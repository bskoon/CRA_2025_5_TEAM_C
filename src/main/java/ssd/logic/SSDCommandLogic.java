package ssd.logic;

import ssd.IO.IOHandler;
import ssd.command.CommandExecutor;

import static ssd.SSDConstant.READ;
import static ssd.SSDConstant.WRITE;

public class SSDCommandLogic {
    private final SSDAppLogic ssdAppLogic;
    private final IOHandler outputIO;

    public static int DEFAULT_ARG_COUNT = 1;
    public static int READ_ARG_COUNT = 2;
    public static int WRITE_ARG_COUNT = 3;

    public SSDCommandLogic(SSDAppLogic ssdAppLogic, IOHandler outputIO) {
        this.ssdAppLogic = ssdAppLogic;
        this.outputIO = outputIO;
    }

    public void run(String[] args) {
        try {
            checkArgument(args);
            char command = args[0].charAt(0);

            CommandExecutor executor = new CommandExecutor(ssdAppLogic);
            executor.execute(command, args);
        } catch (RuntimeException e) {
            outputIO.write(0, "ERROR");
        }
    }

    private static void checkArgument(String[] args) {
        checkValidCommand(args);
        checkArgumentCountForCommand(args, getCommandArgumentCount(args));
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

    private static void checkArgumentCountForCommand(String[] args, int count) {
        if (args.length != count) {
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

    private static void isLBAInteger(String lbaString) {
        try {
            int lba = Integer.parseInt(lbaString);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
