package ssd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SSD {

    public static final String SSD_NAND = "ssd_nand.txt";
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

    public boolean write(int lba, String newData) {
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

    public String read(int i) {
        return null;
    }

    public static void main(String[] args) {
    }
}
