package ssd.IO;

import java.io.BufferedWriter;
import java.io.FileWriter;

import static ssd.SSDConstant.*;

public class OutputIO extends IOHandler {
    public OutputIO(String path) {
        super(path);
        this.write(0, "");
    }

    @Override
    public void write(int targetLine, String newData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(newData);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
