package ssd.IO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static ssd.common.SSDConstant.*;

public class SSDIO extends IOHandler {

    public static final int VALID_SSD_SIZE = 1390;

    public SSDIO(String path) {
        super(path);
        if (isValidSSDExist()) return;
        initSsdNandFile();
    }

    private static void initSsdNandFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SSD_FILE_PATH))) {
            StringBuilder sb = new StringBuilder();
            for (int i = MIN_LBA; i <= MAX_LBA; i++)
                sb.append(i + " " + "0x00000000" + '\n');
            bw.write(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private static boolean isValidSSDExist() {
        File ssdFile = new File(SSD_FILE_PATH);
        return ssdFile.exists() && ssdFile.length() == VALID_SSD_SIZE;
    }

    @Override
    public void write(int targetLine, String newData) {
        List<String> curSSDData = getCurrentSsdData();
        swapDataOnSSD(targetLine, newData, curSSDData);
    }

    private void swapDataOnSSD(int targetLBA, String dataForSpecificLBA, List<String> currentSSDData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            StringBuilder sb = new StringBuilder();
            for (int line = 0; line < currentSSDData.size(); line++) {
                if (line == targetLBA) sb.append(line + " " + dataForSpecificLBA + '\n');
                else sb.append(currentSSDData.get(line) + '\n');
            }
            bw.write(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private List<String> getCurrentSsdData() {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
}
