import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TestShell {
    private static final String NAND_FILE = "ssd_nand.txt";
    private static final String OUTPUT_FILE = "ssd_output.txt";
    private static final int LBA_SIZE = 4;
    private static final int MAX_LBA = 100;

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            launchShell();
            return;
        }       
    }

    public static void launchShell() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("SSD Test Shell 시작 (명령어 입력: write/read)");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            String[] tokens = line.split("\\s+");

            if (tokens.length == 0) continue;
            String cmd = tokens[0].toLowerCase();

            switch (cmd) {
                case "write":
                    if (tokens.length != 3 || !isValidLBA(tokens[1]) || !isValidValue(tokens[2])) {
                        System.out.println("ERROR: 잘못된 입력");
                        break;
                    }
                    int lbaW = Integer.parseInt(tokens[1]);
                    String val = tokens[2].substring(2);
                    writeLBA(lbaW, val);
                    break;

                case "read":
                    if (tokens.length != 2 || !isValidLBA(tokens[1])) {
                        System.out.println("ERROR: 잘못된 입력");
                        break;
                    }
                    int lbaR = Integer.parseInt(tokens[1]);
                    String readVal = readLBA(lbaR);
                    System.out.println("읽은 값: 0x" + readVal);
                    break;

                default:
                    System.out.println("INVALID COMMAND");
            }
        }
    }

    public static boolean isValidLBA(String arg) {
        try {
            int lba = Integer.parseInt(arg);
            return lba >= 0 && lba < MAX_LBA;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidValue(String val) {
        return val.matches("0x[0-9A-F]{8}");
    }

    public static void writeLBA(int lba, String hexValue) throws IOException {
       // String[] nand = loadNand();
        // nand[lba] = hexValue.toUpperCase();
        // saveNand(nand);
    }

    public static String readLBA(int lba) throws IOException {
       // String[] nand = loadNand();
       // return nand[lba];
    }

    public static String[] loadNand() throws IOException {
        Path path = Paths.get(NAND_FILE);
        String[] data = new String[MAX_LBA];
        Arrays.fill(data, "00000000");

        if (!Files.exists(path)) {
            return data;
        }

        List<String> lines = Files.readAllLines(path);
        for (int i = 0; i < Math.min(lines.size(), MAX_LBA); i++) {
            if (lines.get(i).matches("[0-9A-Fa-f]{8}")) {
                data[i] = lines.get(i).toUpperCase();
            }
        }
        return data;
    }

    public static void saveNand(String[] data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(NAND_FILE));
        for (String line : data) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }

    public static void writeOutput(String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));
        if (content.equals("ERROR")) {
            writer.write("ERROR");
        } else {
            writer.write("0x" + content);
        }
        writer.close();
    }
}
