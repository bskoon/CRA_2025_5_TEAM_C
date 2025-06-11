package ssd.IO;

import java.io.BufferedWriter;
import java.io.FileWriter;

import static ssd.SSDConstant.*;

public class OutputIO extends IOHandler{
    public OutputIO(String path) {
        super(path);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH))) {
            bw.write("");
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
