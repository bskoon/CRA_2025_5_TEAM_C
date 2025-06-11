package ssd.IO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class IOHandler {
    public String path;

    public IOHandler(String path) {
        this.path = path;
    }

    public abstract void write(int targetLine, String newData);

    public String read(int lba) {
        String ssdData = "";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
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
}
