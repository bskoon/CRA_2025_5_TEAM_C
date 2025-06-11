package ssd.logic;

import ssd.IO.IOHandler;
import ssd.SSDConstant;

public class SSDCommandLogic {
    private final SSDAppLogic ssdAppLogic;
    private final IOHandler ioHandler;

    public static int DEFAULT_ARG_COUNT = 1;
    public static int READ_ARG_COUNT = 2;
    public static int WRITE_ARG_COUNT = 3;

    public SSDCommandLogic(SSDAppLogic ssdAppLogic, IOHandler ioHandler) {
        this.ssdAppLogic = ssdAppLogic;
        this.ioHandler = ioHandler;
    }

    public void run(String[] args) {
        // todo :: main 로직 여기로 이동
        // todo :: rw 만 판단 다음 로직은 SsdAppLogic 처리
    }

    public static void main(String[] args) {
        checkArgument(args);
        char command = args[0].charAt(0);

        switch (command) {
            case SSDConstant.READ:
                read(Integer.parseInt(args[1]));
                break;
            case SSDConstant.WRITE:
                write(Integer.parseInt(args[1]), args[2]);
                break;
            default:
                throw new RuntimeException();
        }
    }

    private static void checkArgument(String[] args) {
        checkValidCommand(args);
        checkArgumentCount(args, getCommandArgumentCount(args));
        isLBAInteger(args[1]);
    }

    private static void checkValidCommand(String[] args) {
        isArgumentHaveCommand(args);
        isArgumentValidValue(args);
    }

    private static void isArgumentHaveCommand(String[] args) {
        if (args[0].length() > 1) {
            throw new RuntimeException();
        }
    }

    private static void isArgumentValidValue(String[] args) {
        if (args[0].charAt(0) < 'A' || args[0].charAt(0) > 'Z') {
            throw new RuntimeException();
        }
    }

    private static int getCommandArgumentCount(String[] args) {
        int count;
        char command = args[0].charAt(0);
        switch (command) {
            case READ:
                count = READ_ARG_COUNT;
                break;
            case WRITE:
                count = WRITE_ARG_COUNT;
                break;
            default:
                count = DEFAULT_ARG_COUNT;
                break;
        }
        return count;
    }

    private static void checkArgumentCount(String[] args, int count) {
        if (args.length != count) {
            throw new RuntimeException();
        }
    }

    private static void isLBAInteger(String lbaString) {
        try {
            int lba = Integer.parseInt(lbaString);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
