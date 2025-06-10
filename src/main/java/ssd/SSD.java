package ssd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SSD {

    public static final String SSD_NAND = "ssd_nand.txt";

    public SSD() {
        if (new File(SSD_NAND).exists()) return;
        initSsdNandFile();
    }

    private static void initSsdNandFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SSD_NAND))) {
            StringBuilder sb = new StringBuilder();
            for (int i=0 ; i<100 ; i++)
                sb.append(i + " " + "0x00000000" + '\n');
            bw.write(sb.toString());
        } catch (Exception e){
            throw new RuntimeException();
        }
    }

    public boolean write(int lba, String newData) {
        checkArgument(lba, newData);
        writeDataOnSsd(lba, newData, getCurrentSsdData());
        return true;
    }

    private static void checkArgument(int lba, String newData) {
        checkValidLBA(lba);
        checkDataStartWith0x(newData);
        checkDataLength(newData);
        checkValidData(newData);
    }

    private static void checkValidLBA(int lba) {
        if (lba < 0 || lba > 99) throw new RuntimeException();
    }

    private static void checkDataStartWith0x(String newData) {
        if (!newData.startsWith("0x")) throw new RuntimeException();
    }

    private static void checkDataLength(String newData) {
        if (newData.length() != 10) throw new RuntimeException();
    }

    private static void checkValidData(String newData) {
        char[] strArr = newData.toCharArray();
        for (int j = 2 ; j<strArr.length ; j++){
            if (!((strArr[j] >= '0' && strArr[j] <= '9') || (strArr[j] >= 'A' && strArr[j] <= 'F'))) throw new RuntimeException();
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
            for (int lba=0 ; lba<100 ; lba++){
                if (lba == targetLba) sb.append(lba + " " + newData + '\n');
                else sb.append(curData.get(lba) + '\n');
            }
            bw.write(sb.toString());
        } catch (Exception e){
            throw new RuntimeException();
        }
    }

    public String read(int i) {
        return null;
    }

    public static void main(String[] args) {
    }
}
