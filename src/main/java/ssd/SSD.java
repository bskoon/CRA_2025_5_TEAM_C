package ssd;

import java.io.*;

public class SSD {

    public static final int DEFAULT_ARG_COUNT = 1;
    public static final int READ_ARG_COUNT = 2;
    public static final int WRITE_ARG_COUNT = 3;
    public static final char READ = 'R';
    public static final char WRITE = 'W';
    public static final String SSD_NAND = "ssd_nand.txt";
    public static final String SSD_OUTPUT = "ssd_output.txt";

    public boolean write(int i, String s) {
        return false;
    }

    public String read(int lba) {
        checkLBARange(lba);
        String ssdData = getDataFromSSD(lba);
        writeDataOnOutFile(ssdData);
        return ssdData;
    }

    private static String getDataFromSSD(int lba) {
        String ssdData = "";

        try (BufferedReader br = new BufferedReader(new FileReader(SSD_NAND))) {
            String line;
            while((line = br.readLine()) != null ) {
                String[] ssdRawData = line.split(" ");
                if (ssdRawData[0].equals(Integer.toString(lba))) {
                    ssdData = ssdRawData[1];
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Read Exception");
        }
        return ssdData;
    }

    private static void writeDataOnOutFile(String ssdData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SSD_OUTPUT))) {
            bw.write(ssdData);
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Read Exception");
        }
    }

    private static void checkLBARange(int i) {
        if (i < 0 || i >= 100) {
            throw new RuntimeException("Error");
        }
    }

    public void main(String[] args) {
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

    private void checkArgument(String[] args) {
        checkValidCommand(args);
        checkArgumentCount(args, getCommandArgumentCount(args));
        isLBAInteger(args[1]);
    }

    private void checkValidCommand(String[] args) {
        isArgumentHaveCommand(args);
        isArgumentValidValue(args);
    }

    private void isArgumentHaveCommand(String[] args) {
        if (args[0].length() > 1) {
            throw new RuntimeException();
        }
    }

    private void isArgumentValidValue(String[] args) {
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

    private void checkArgumentCount(String[] args, int count) {
        if (args.length != count) {
            throw new RuntimeException();
        }
    }

    private void isLBAInteger(String lbaString) {
        try {
            int lba = Integer.parseInt(lbaString);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
