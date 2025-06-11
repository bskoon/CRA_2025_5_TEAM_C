package ssd;

import java.io.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SSD {

    public SSD() {
        if (isValidSSDExist()) {
            return;
        }
        initSsdNandFile();
    }

    private static boolean isValidSSDExist() {
        File ssdFile = new File(SSD_NAND);
        return ssdFile.exists() && ssdFile.length() == VALID_SSD_SIZE;
    }

    private static void initSsdNandFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SSD_NAND))) {
            StringBuilder sb = new StringBuilder();
            for (int i = MIN_LBA; i <= MAX_LBA; i++)
                sb.append(i + " " + "0x00000000" + '\n');
            bw.write(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }



    public static void main(String[] args) {
        checkArgument(args);
        char command = args[0].charAt(0);

        switch (command) {
            case READ:
                read(Integer.parseInt(args[1]));
                break;
            case WRITE:
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
