package ssd.command;

public enum CommandType {
    W, R, E;

    public static CommandType fromString(String value) {
        if (value == null) {
            throw new RuntimeException("명령 문자열이 null입니다.");
        }

        return switch (value) {
            case "W" -> W;
            case "R" -> R;
            case "E" -> E;
            default -> throw new RuntimeException("알 수 없는 명령: " + value);
        };
    }
}
