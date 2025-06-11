package shell;

import java.util.*;

public class TestShell {

    private static Scanner scanner;
    private static boolean isRunning;
    private static final int MAX_LBA = 100;


    public TestShell() {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    public static void main(String[] args) {
        TestShell shell = new TestShell();
        launchShell();
    }

    public static void launchShell() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("SSD Test Shell 시작 (명령어 입력: write/read)");

        while (isRunning) {
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
                case "exit":
                    exit();
                    break;
                case "help":
                    help();
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

    public static void writeLBA(int lba, String hexValue) {
    }

    public static String readLBA(int lba) {
        return "";
    }

    public static void exit() {
        System.out.println("Exiting TestShell...");
        isRunning = false;
        if (scanner != null) {
            scanner.close();
        }
    }

    public static void help() {
        System.out.println("==========================================");
        System.out.println("TestShell Help");
        System.out.println("==========================================");
        System.out.println("제작자: TEAM_C");
        System.out.println();
        System.out.println("사용 가능한 명령어:");
        System.out.println();
        System.out.println("• write");
        System.out.println("  - 설명: SSD의 특정 LBA 영역에 데이터를 저장합니다.");
        System.out.println("  - 사용법: ssd W [LBA] [DATA]");
        System.out.println("  - 예시: ssd W 3 0x1298CDEF");
        System.out.println("  - 설명: 3번 LBA 영역에 값 0x1298CDEF를 저장한다.");
        System.out.println();
        System.out.println("• read");
        System.out.println("  - 설명: SSD의 특정 LBA 영역에서 데이터를 읽어옵니다.");
        System.out.println("  - 사용법: ssd R [LBA]");
        System.out.println("  - 예시: ssd R 2");
        System.out.println("  - 출력결과: 0xAAAABBBB");
        System.out.println();
        System.out.println("• exit");
        System.out.println("  - 설명: TestShell을 종료합니다.");
        System.out.println();
        System.out.println("• help");
        System.out.println("  - 설명: 이 도움말을 표시합니다.");
        System.out.println("==========================================");
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}
