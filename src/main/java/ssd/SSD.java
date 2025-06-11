package ssd;

import java.io.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SSD {
    public static final int DEFAULT_ARG_COUNT = 1;
    public static final int READ_ARG_COUNT = 2;
    public static final int WRITE_ARG_COUNT = 3;
    public static final char READ = 'R';
    public static final char WRITE = 'W';
    public static final String SSD_NAND = "ssd_nand.txt";
    public static final String SSD_OUTPUT = "ssd_output.txt";
    public static final int MIN_LBA = 0;
    public static final int MAX_LBA = 99;

    public SSD() {
        if (new File(SSD_NAND).exists()) return;
        initSsdNandFile();
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

    public static boolean write(int lba, String newData) {
        checkWriteDataArgument(lba, newData);
        writeDataOnSsd(lba, newData, getCurrentSsdData());
        return true;
    }

    private static void checkWriteDataArgument(int lba, String newData) {
        checkValidLBA(lba);
        checkDataStartWith0x(newData);
        checkDataLength(newData);
        checkValidData(newData);
    }

    private static void checkValidLBA(int lba) {
        if (lba < MIN_LBA || lba > MAX_LBA) throw new RuntimeException();
    }

    private static void checkDataStartWith0x(String newData) {
        if (!newData.startsWith("0x")) throw new RuntimeException();
    }

    private static void checkDataLength(String newData) {
        if (newData.length() != 10) throw new RuntimeException();
    }

    private static void checkValidData(String newData) {
        char[] strArr = newData.toCharArray();
        for (int j = 2; j < strArr.length; j++) {
            if (!((strArr[j] >= '0' && strArr[j] <= '9') || (strArr[j] >= 'A' && strArr[j] <= 'F')))
                throw new RuntimeException();
        }
    }

    private static List<String> getCurrentSsdData() {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(SSD_NAND));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    private static void writeDataOnSsd(int targetLba, String newData, List<String> curData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SSD_NAND))) {
            StringBuilder sb = new StringBuilder();
            for (int lba = MIN_LBA; lba <= MAX_LBA; lba++) {
                if (lba == targetLba) sb.append(lba + " " + newData + '\n');
                else sb.append(curData.get(lba) + '\n');
            }
            bw.write(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static String read(int lba) {
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
