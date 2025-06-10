package ssd;

import java.io.*;

public class SSD {
    public boolean write(int i, String s) {
        return false;
    }

    public String read(int i) {
        String ret = "";

        if (i < 0 || i >= 100) {
            throw new RuntimeException("Error");
        }

        try (BufferedReader br = new BufferedReader(new FileReader("ssd_nand.txt"))) {
            String line;
            while((line = br.readLine()) != null ) {
                String[] temp = line.split(" ");
                if (temp[0].equals(Integer.toString(i))) {
                    ret = temp[1];
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Read Exception");
        }


        try (BufferedWriter bw = new BufferedWriter(new FileWriter("ssd_nand.txt"))) {
            bw.write(ret);
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Read Exception");
        }

        return ret;
    }

    public void main(String[] args) {
        if (args[0].length() > 1) {
            throw new RuntimeException();
        }
        if (args[0].charAt(0) < 'A' || args[0].charAt(0) > 'Z') {
            throw new RuntimeException();
        }

        char command = args[0].charAt(0);

        if (command == 'R') {
            if (args.length != 2) {
                throw new RuntimeException();
            }
            int lba = -1;
            try {
                lba = Integer.parseInt(args[1]);
            } catch (Exception e) {
                throw new RuntimeException();
            }
            read(lba);
        } else if (command == 'W') {
            if (args.length != 3) {
                throw new RuntimeException();
            }
            int lba = -1;
            try {
                lba = Integer.parseInt(args[1]);
            } catch (Exception e) {
                throw new RuntimeException();
            }
            write(lba, args[2]);
        }

    }
}
