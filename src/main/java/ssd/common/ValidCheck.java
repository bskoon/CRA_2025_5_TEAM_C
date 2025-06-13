package ssd.common;

public class ValidCheck {
    public static boolean isInRange0to99(String value) {
        return value != null && value.matches("[0-9]{1,2}");
    }

    public static boolean isValidHex32(String value) {
        if (value == null || !value.matches("^0x[0-9A-Fa-f]{8}$")) {
            return false;
        }

        try {
            // Java는 부호 없는 정수를 지원하지 않기 때문에 long으로 파싱
            long number = Long.parseLong(value.substring(2), 16);
            return number >= 0 && number <= 0xFFFFFFFFL;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isStringBetween0And10(String value) {
        try {
            int num = Integer.parseInt(value);
            return num >= 0 && num <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void validateCommandType(String value) {
        if (!"W".equals(value) && !"R".equals(value) && !"E".equals(value)) {
            throw new RuntimeException("지원하지 않는 명령어입니다: " + value);
        }
    }
}
