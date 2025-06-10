package ssd;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SSD {

    public SSD() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("ssd_nand.txt"))) {
            StringBuilder sb = new StringBuilder();
            for (int i=0 ; i<100 ; i++)
                sb.append(i + " " + "0x00000000" + '\n');
            bw.write(sb.toString());
        } catch (Exception e){

        }
    }

    public boolean write(int i, String s) {
        if (i < 0 || i > 99) throw new RuntimeException();
        if (!s.contains("0x")) throw new RuntimeException();
        if (s.length() != 10) throw new RuntimeException();
        char[] strArr = s.toCharArray();
        for (int j = 2 ; j<strArr.length ; j++){
            if (!((strArr[j] >= '0' && strArr[j] <= '9') || (strArr[j] >= 'A' && strArr[j] <= 'F'))) throw new RuntimeException();
        }
        String filePath = "ssd_nand.txt";
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(filePath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            StringBuilder sb = new StringBuilder();
            for (int j=0 ; j<100 ; j++){
                if (j == i) sb.append(j + " " + s + '\n');
                else sb.append(lines.get(j) + '\n');
            }
            bw.write(sb.toString());
        } catch (Exception e){
            throw new RuntimeException();
        }

        return true;
    }

    public String read(int i) {
        return null;
    }

    public static void main(String[] args) {
    }
}
