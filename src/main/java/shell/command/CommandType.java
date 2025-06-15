package shell.command;

import static shell.util.ShellConstant.*;

public enum CommandType {
    read(2),
    write(3),
    fullread(1),
    fullwrite(2),
    erase(3),
    erase_range(3),
    flush(1),
    scenario1(1),
    scenario2(1),
    scenario3(1),
    scenario4(1);

    private final int argCount;

    CommandType(int argCount) {
        this.argCount = argCount;
    }

    public static CommandType fromString(String value) {
        if (value == null) {
            throw new RuntimeException("명령 문자열이 null입니다.");
        }

        return switch (value) {
            case READ -> read;
            case WRITE -> write;
            case FULLREAD -> fullread;
            case FULLWRITE -> fullwrite;
            case ERASE -> erase;
            case ERASERANGE -> erase_range;
            case FLUSH -> flush;
            case SCENARIO_1 -> scenario1;
            case SCENARIO_2 -> scenario2;
            case SCENARIO_3 -> scenario3;
            case SCENARIO_4 -> scenario4;
            default -> throw new RuntimeException("알 수 없는 명령: " + value);
        };
    }

    public int getArgCount() {
        return argCount;
    }
}
