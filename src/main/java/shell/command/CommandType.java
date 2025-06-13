package shell.command;

import static shell.util.ShellConstant.*;

public enum CommandType {
    read, write, fullread, fullwrite, erase, erase_range, flush, script1, script2, script3, script4;

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
            case SCENARIO_1 -> script1;
            case SCENARIO_2 -> script2;
            case SCENARIO_3 -> script3;
            case SCENARIO_4 -> script4;
            default -> throw new RuntimeException("알 수 없는 명령: " + value);
        };
    }
}
