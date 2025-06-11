package ssd.IO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class IOHandler {
    public String path;

    public IOHandler(String path) {
        this.path = path;
    }

    public void write(int targetLine, String newData) {
        List<String> curData = getCurrentSsdData();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            StringBuilder sb = new StringBuilder();
            if (curData.isEmpty())
                sb.append(newData);
            else {
                for (int line = 0; line < curData.size(); line++) {
                    if (line == targetLine) sb.append(line + " " + newData + '\n');
                    else sb.append(curData.get(line) + '\n');
                }
            }
            bw.write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
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
