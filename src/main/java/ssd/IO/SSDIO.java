package ssd.IO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static ssd.SSDConstant.*;

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
}
