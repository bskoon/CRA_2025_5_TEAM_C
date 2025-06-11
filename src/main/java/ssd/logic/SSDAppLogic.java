package ssd.logic;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.SSDConstant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static ssd.SSDConstant;

public class SSDAppLogic {
    private final OutputIO outputIO;
    private final SSDIO ssdIO;

    public SSDAppLogic(OutputIO outputIO, SSDIO ssdIO) {
        this.outputIO = outputIO;
        this.ssdIO = ssdIO;
    }

    public void handler(String command, int lba, String value) {
        // todo :: ssd read/write 기능 수행 및 에러 발생시 output IO 처리
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
        if (lba < SSDConstant.MIN_LBA || lba > SSDConstant.MAX_LBA) {
            throw new RuntimeException();
        }
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
}
