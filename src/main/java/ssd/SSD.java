package ssd;

import java.io.*;

public class SSD {

    public static final int READ_ARG_COUNT = 2;
    public static final int WRITE_ARG_COUNT = 3;

    public boolean write(int i, String s) {
        return false;
    }

    public String read(int lba) {
        String ssdData = "";
        checkLBARange(lba);

        try (BufferedReader br = new BufferedReader(new FileReader("ssd_nand.txt"))) {
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

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("ssd_nand.txt"))) {
            bw.write(ssdData);
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Read Exception");
        }

        return ssdData;
    }

    private static void checkLBARange(int i) {
        if (i < 0 || i >= 100) {
            throw new RuntimeException("Error");
        }
    }

    public void main(String[] args) {
        checkCommandArgument(args);
        char command = args[0].charAt(0);

        if (command == 'R') {
            checkArgument(args, READ_ARG_COUNT);

            int lba = Integer.parseInt(args[1]);
            read(lba);
        } else if (command == 'W') {
            checkArgument(args, WRITE_ARG_COUNT);

            int lba = Integer.parseInt(args[1]);
            String ssdData = args[2];
            write(lba, ssdData);
        }
    }

    private static void checkCommandArgument(String[] args) {
        if (args[0].length() > 1) {
            throw new RuntimeException();
        }
        if (args[0].charAt(0) < 'A' || args[0].charAt(0) > 'Z') {
            throw new RuntimeException();
        }
    }

    private void checkArgument(String[] args, int count) {
        checkArgumentCount(args, count);
        isLBAInteger(args[1]);
    }

    private static void checkArgumentCount(String[] args, int count) {
        if (args.length != count) {
            throw new RuntimeException();
        }
    }

    private void isLBAInteger(String lbaString) {
        try {
            int lba = Integer.parseInt(lbaString);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
