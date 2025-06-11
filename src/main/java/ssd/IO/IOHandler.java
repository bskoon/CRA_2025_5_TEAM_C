package ssd.IO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class IOHandler {
    public String path;

    public IOHandler(String path) {
        this.path = path;
    }

    public abstract void write(int targetLine, String newData);

    public String read(int lineNum) {
        String ssdData = "";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            ssdData = readSpecificLineDataFromFile(lineNum, br, ssdData);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return ssdData;
    }

    private static String readSpecificLineDataFromFile(int lineNum, BufferedReader br, String ssdData) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            String[] ssdRawData = line.split(" ");
            if (ssdRawData[0].equals(Integer.toString(lineNum))) {
                ssdData = ssdRawData[1];
                break;
            }
        }
        return ssdData;
    }
}
