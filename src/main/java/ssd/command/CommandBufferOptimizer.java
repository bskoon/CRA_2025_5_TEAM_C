package ssd.command;

import java.util.*;

public class CommandBufferOptimizer {

    public static List<String> optimize(List<String> bufferCommands) {
        List<Command> parsed = parseCommands(bufferCommands);
        List<Command> result = new ArrayList<>();

        for (Command current : parsed) {

            // 중복 Write 제거 (같은 LBA에 대해 최신 값 유지)
            result.removeIf(existing ->
                    existing.isWrite() &&
                            current.isWrite() &&
                            existing.lba == current.lba
            );

            // Erase가 이전 Write를 덮는 경우 Write 제거
            if (current.isErase()) {
                result.removeIf(existing ->
                        existing.isWrite() &&
                                current.covers(existing.lba)
                );
            }

            // Write가 이전 Erase의 일부를 덮는 경우:
            // Erase가 단일 LBA만 지우고, 그 LBA에 Write가 들어오면 해당 Erase 제거
            if (current.isWrite()) {
                Iterator<Command> it = result.iterator();
                while (it.hasNext()) {
                    Command existing = it.next();
                    if (existing.isErase() && existing.covers(current.lba) && existing.size == 1) {
                        it.remove(); // 단일 LBA만 지우는 Erase 제거
                    }
                }
            }

            // Erase 병합 로직
            if (current.isErase()) {
                Optional<Command> overlapping = result.stream()
                        .filter(e -> e.isErase() && e.overlapsOrTouches(current))
                        .findFirst();

                if (overlapping.isPresent()) {
                    Command prev = overlapping.get();
                    int newStart = Math.min(prev.lba, current.lba);
                    int newEnd = Math.max(prev.lba + prev.size, current.lba + current.size);
                    prev.lba = newStart;
                    prev.size = newEnd - newStart;

                    // 병합 후 write 제거
                    result.removeIf(existing ->
                            existing.isWrite() &&
                                    prev.covers(existing.lba)
                    );
                    continue;
                }
            }

            result.add(current);
        }

        // 최종 번호 재정렬
        return toCommandStrings(result);
    }

    public static Optional<String> fastRead(List<String> bufferCommands, int readLBA) {
        List<Command> parsed = parseCommands(bufferCommands);
        Collections.reverse(parsed);

        for (Command cmd : parsed) {
            if (cmd.isWrite() && cmd.lba == readLBA) {
                return Optional.of(cmd.value);
            } else if (cmd.isErase() && cmd.covers(readLBA)) {
                return Optional.of("0x00000000");
            }
        }
        return Optional.empty();
    }

    private static List<Command> parseCommands(List<String> buffer) {
        List<Command> list = new ArrayList<>();
        for (String line : buffer) {
            String[] split = line.split("_");
            if (split.length < 4) continue;

            String type = split[1];
            int lba = Integer.parseInt(split[2]);

            if (type.equals("W")) {
                String value = split[3];
                list.add(new Command(type, lba, 0, value));
            } else if (type.equals("E")) {
                int size = Integer.parseInt(split[3]);
                list.add(new Command(type, lba, size, null));
            }
        }
        return list;
    }

    private static List<String> toCommandStrings(List<Command> commands) {
        List<String> output = new ArrayList<>();
        int index = 1;
        for (Command cmd : commands) {
            if (cmd.isWrite()) {
                output.add(index + "_W_" + cmd.lba + "_" + cmd.value);
            } else if (cmd.isErase()) {
                output.add(index + "_E_" + cmd.lba + "_" + cmd.size);
            }
            index++;
        }
        return output;
    }

    private static class Command {
        String type;
        int lba;
        int size;
        String value;

        Command(String type, int lba, int size, String value) {
            this.type = type;
            this.lba = lba;
            this.size = size;
            this.value = value;
        }

        boolean isWrite() {
            return "W".equals(type);
        }

        boolean isErase() {
            return "E".equals(type);
        }

        boolean covers(int targetLBA) {
            return isErase() && lba <= targetLBA && targetLBA < lba + size;
        }

        boolean overlapsOrTouches(Command other) {
            return isErase() && other.isErase() &&
                    (this.lba <= other.lba + other.size - 1 &&
                            other.lba <= this.lba + this.size - 1);
        }
    }
}
