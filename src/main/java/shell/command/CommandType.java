package shell.command;

public enum CommandType {
    read, write, fullread, fullwrite, erase, erase_range, flush, script1, script2, script3, script4;

    public static CommandType fromString(String value) {
        if (value == null) {
            throw new RuntimeException("명령 문자열이 null입니다.");
        }

        return switch (value) {
            case "read" -> read;
            case "write" -> write;
            case "fullread" -> fullread;
            case "fullwrite" -> fullwrite;
            case "erase" -> erase;
            case "erase_range" -> erase_range;
            case "flush" -> flush;
            case "1_fullwriteandreadcompare" -> script1;
            case "2_partiallbawrite" -> script2;
            case "3_writereadaging" -> script3;
            case "4_writereadaging" -> script4;
            default -> throw new RuntimeException("알 수 없는 명령: " + value);
        };
    }
}
