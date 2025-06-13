package ssd.command;

import java.util.*;
import java.util.stream.Collectors;

public class CommandBufferOptimizer {

    private static final int MAX_ERASE_SIZE = 10;

    public static List<String> optimize(List<String> bufferCommands) {
        List<Command> parsed = parseCommands(bufferCommands);

        Map<Integer, Command> writeMap = new HashMap<>();
        List<Command> eraseList = new ArrayList<>();

        for (Command current : parsed) {
            if (current.isWrite()) {
                // 중복 write 제거 및 최신화
                writeMap.put(current.lba, current);

                // 기존 erase 중 current가 완전히 덮는 경우 제거
                eraseList.removeIf(erase -> erase.covers(current.lba) && isFullyCoveredByWrites(erase, writeMap));

            } else if (current.isErase()) {
                // erase가 덮는 write 제거
                for (int i = current.lba; i < current.lba + current.size; i++) {
                    writeMap.remove(i);
                }

                // 병합 대상 탐색
                List<Command> mergeTargets = new ArrayList<>();
                for (Command e : eraseList) {
                    if (e.overlapsOrTouches(current)) {
                        mergeTargets.add(e);
                    }
                }

                // 병합 처리
                if (!mergeTargets.isEmpty()) {
                    int minLba = Math.min(current.lba, mergeTargets.stream().mapToInt(e -> e.lba).min().orElse(current.lba));
                    int maxLba = Math.max(current.lba + current.size,
                            mergeTargets.stream().mapToInt(e -> e.lba + e.size).max().orElse(current.lba + current.size));

                    List<Integer> effectiveLbas = new ArrayList<>();
                    for (int i = minLba; i < maxLba; i++) {
                        if (!writeMap.containsKey(i)) {
                            effectiveLbas.add(i);
                        }
                    }

                    if (effectiveLbas.size() <= MAX_ERASE_SIZE) {
                        eraseList.removeAll(mergeTargets);
                        addEraseSegments(effectiveLbas, eraseList);
                        continue; // current 병합 완료
                    }
                }

                eraseList.add(current);
            }
        }

        List<Command> combined = new ArrayList<>(eraseList);
        combined.addAll(writeMap.values());
        combined.sort(Comparator.comparingInt(cmd -> cmd.lba));

        return toCommandStrings(combined);
    }

    private static boolean isFullyCoveredByWrites(Command erase, Map<Integer, Command> writeMap) {
        for (int i = erase.lba; i < erase.lba + erase.size; i++) {
            if (!writeMap.containsKey(i)) return false;
        }
        return true;
    }

    private static void addEraseSegments(List<Integer> lbas, List<Command> result) {
        for (int i = 0; i < lbas.size(); ) {
            int start = lbas.get(i);
            int j = i;
            while (j + 1 < lbas.size() && lbas.get(j + 1) == lbas.get(j) + 1) j++;
            result.add(new Command("E", start, lbas.get(j) - start + 1, null));
            i = j + 1;
        }
    }

    public static Optional<String> fastRead(List<String> bufferCommands, int readLBA) {
        List<Command> parsed = parseCommands(bufferCommands);
        Collections.reverse(parsed);
        for (Command cmd : parsed) {
            if (cmd.isWrite() && cmd.lba == readLBA) return Optional.of(cmd.value);
            if (cmd.isErase() && cmd.covers(readLBA)) return Optional.of("0x00000000");
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
                list.add(new Command("W", lba, 0, split[3]));
            } else if (type.equals("E")) {
                int size = Integer.parseInt(split[3]);
                list.add(new Command("E", lba, size, null));
            }
        }
        return list;
    }

    private static List<String> toCommandStrings(List<Command> commands) {
        List<String> result = new ArrayList<>();
        int index = 1;
        for (Command cmd : commands) {
            if (cmd.isWrite()) {
                result.add(index++ + "_W_" + cmd.lba + "_" + cmd.value);
            } else {
                result.add(index++ + "_E_" + cmd.lba + "_" + cmd.size);
            }
        }
        return result;
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
            int thisEnd = this.lba + this.size - 1;
            int otherEnd = other.lba + other.size - 1;
            return thisEnd + 1 >= other.lba && otherEnd + 1 >= this.lba;
        }
    }
}

