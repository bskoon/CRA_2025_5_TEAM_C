package shell;

import java.io.*;
import java.nio.file.*;
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
                case "fullwrite":
                    if (tokens.length != 2 || !isValidValue(tokens[1])) {
                        System.out.println("INVALID COMMAND");
                        break;
                    }
                    fullWrite(tokens[1]);
                    break;

                case "fullread":
                    if (tokens.length != 1) {
                        System.out.println("INVALID COMMAND");
                        break;
                    }
                    fullRead();
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
        callSsdWriteProcess(lba,hexValue);
    }

    private static void callSsdWriteProcess(int lba, String hexValue){
        // 실행할 .jar 파일 경로와 명령어 인자를 설정
        String jarFilePath = "/C:/SSD.jar";  // .jar 파일의 경로
        String inputCommand = "W "+lba+ " "+ hexValue;  // 입력할 명령어

        // ProcessBuilder 생성
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-jar", jarFilePath, inputCommand
        );

        // 프로세스 실행
        try {
            Process process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readLBA(int lba) {
        return callSsdReadProcess(lba);
    }

    public static void fullWrite(String hexValue) {
        for (int lba = 0; lba < MAX_LBA; lba++) {
            writeLBA(lba, hexValue);
        }
        System.out.println("[Write] Done");
    }

    public static void fullRead() {
        for (int lba = 0; lba < MAX_LBA; lba++) {
            String value = readLBA(lba);
            System.out.println("LBA " + String.format("%02d", lba) + ": " + value);
        }
    }

    private static String callSsdReadProcess(int lba) {
        // 실행할 .jar 파일 경로와 명령어 인자를 설정
        String jarFilePath = "C:\\SSD.jar";  // .jar 파일의 경로
        String inputCommand = "R "+ lba;  // 입력할 명령어

        // ProcessBuilder 생성
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-jar", jarFilePath, inputCommand
        );
        // 실행 디렉토리를 C:\ 로 설정
        processBuilder.directory(new File("C:\\"));

        // 오류도 출력에 포함
        processBuilder.redirectErrorStream(true);

        StringBuilder output = new StringBuilder();
        Process process = null;
        try {
            process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
            // 프로세스가 종료될 때까지 대기
            int exitCode = process.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return output.toString().trim();  // 결과 반환;
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
        System.out.println("김범석 bskoon");
        System.out.println("김상윤 huihihet");
        System.out.println("김창지 Chang-ji");
        System.out.println("최재형 jae637");
        System.out.println("김태엽 taeyeob-kim-09");
        System.out.println("노웅규 nohog94");
        System.out.println();
        System.out.println("사용 가능한 명령어:");
        System.out.println();
        System.out.println("• write");
        System.out.println("  - 설명: SSD의 특정 LBA 영역에 데이터를 저장합니다.");
        System.out.println("  - 사용법: write [LBA] [DATA]");
        System.out.println("  - 예시: write 3 0x1298CDEF");
        System.out.println();
        System.out.println("• read");
        System.out.println("  - 설명: SSD의 특정 LBA 영역에서 데이터를 읽어옵니다.");
        System.out.println("  - 사용법: read [LBA]");
        System.out.println("  - 예시: read 2");
        System.out.println();
        System.out.println("• fullwrite");
        System.out.println("  - 설명: 모든 LBA 영역(0~99)에 동일한 데이터를 저장합니다.");
        System.out.println("  - 사용법: fullwrite [DATA]");
        System.out.println("  - 예시: fullwrite 0xABCDFFFF");
        System.out.println();
        System.out.println("• fullread");
        System.out.println("  - 설명: LBA 0번부터 99번까지 모든 데이터를 읽어 출력합니다.");
        System.out.println("  - 사용법: fullread");
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
