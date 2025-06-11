package shell;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TestShell {

    private Scanner scanner;
    private boolean isRunning;
    private static final int MAX_LBA = 100;

    public TestShell() {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    public static void main(String[] args) {
        TestShell shell = new TestShell();
        shell.launchShell();
    }

    public void launchShell() {
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
                    System.out.println("읽은 값: " + readVal);
                    break;
                    
                case "exit":
                    exit();
                    break;
                    
                case "fullwrite":
                    if (tokens.length != 2 || !isValidValue(tokens[1])) {
                        System.out.println("ERROR: 잘못된 입력 (사용법: fullwrite 0xABCDFFFF)");
                        break;
                    }
                    String hexValue = tokens[1].substring(2); // 0x 제거
                    fullWrite(hexValue);
                    break;

                case "fullread":
                    if (tokens.length != 1) {
                        System.out.println("ERROR: fullread는 추가 인자가 필요하지 않습니다");
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

    public boolean isValidLBA(String arg) {
        try {
            int lba = Integer.parseInt(arg);
            return lba >= 0 && lba < MAX_LBA;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isValidValue(String val) {
        return val.matches("0x[0-9A-F]{8}");
    }

    public void writeLBA(int lba, String hexValue) {
        callSsdWriteProcess(lba, hexValue);
    }

    private void callSsdWriteProcess(int lba, String hexValue) {
        // 실행할 .jar 파일 경로와 명령어 인자를 설정
        String jarFilePath = "C:/SSD.jar";  // .jar 파일의 경로
        String inputCommand = "W " + lba + " 0x" + hexValue;  // 입력할 명령어

        // ProcessBuilder 생성
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-jar", jarFilePath, inputCommand
        );

        // 프로세스 실행
        try {
            Process process = processBuilder.start();
            process.waitFor(); // 프로세스 완료 대기
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String readLBA(int lba) {
        return callSsdReadProcess(lba);
    }

    /**
     * fullwrite 명령어 구현
     * 모든 LBA 영역(0~99)에 대해 Write를 수행한다.
     * 
     * @param hexValue 16진수 값 (8자리, 예: ABCDFFFF)
     */
    public void fullWrite(String hexValue) {
        System.out.println("Full write 시작...");
        System.out.println("모든 LBA에 0x" + hexValue + " 저장 중...");
        
        // LBA 0번부터 99번까지 모든 영역에 쓰기
        for (int lba = 0; lba < MAX_LBA; lba++) {
            writeLBA(lba, hexValue);
            
            // 진행상황 출력 (10개씩)
            if ((lba + 1) % 10 == 0) {
                System.out.println("Write 진행: " + (lba + 1) + "/" + MAX_LBA + " LBA 완료");
            }
        }
        
        System.out.println("Full write 완료: 모든 LBA에 0x" + hexValue + " 저장됨");
    }

    /**
     * fullread 명령어 구현
     * LBA 0번부터 99번까지 Read를 수행하고 모든 값을 화면에 출력한다.
     */
    public void fullRead() {
        System.out.println("Full read 시작...");
        System.out.println("==========================================");
        
        // LBA 0번부터 99번까지 모든 영역 읽기
        for (int lba = 0; lba < MAX_LBA; lba++) {
            String value = readLBA(lba);
            // 출력 형식: LBA XX: 값
            System.out.println("LBA " + String.format("%02d", lba) + ": " + value);
        }
        
        System.out.println("==========================================");
        System.out.println("Full read 완료");
    }

    private String callSsdReadProcess(int lba) {
        // 실행할 .jar 파일 경로와 명령어 인자를 설정
        String jarFilePath = "C:\\SSD.jar";  // .jar 파일의 경로
        String inputCommand = "R " + lba;  // 입력할 명령어

        // ProcessBuilder 생성
        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-jar", jarFilePath, inputCommand
        );
        // 실행 디렉토리를 C:\ 로 설정
        processBuilder.directory(new File("C:\\"));

        // 오류도 출력에 포함
        processBuilder.redirectErrorStream(true);

        StringBuilder output = new StringBuilder();
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
            // 프로세스가 종료될 때까지 대기
            int exitCode = process.waitFor();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

        return output.toString().trim();  // 결과 반환
    }

    public void exit() {
        System.out.println("Exiting TestShell...");
        isRunning = false;
        if (scanner != null) {
            scanner.close();
        }
    }

    public void help() {
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
        System.out.println("  - 설명: 3번 LBA 영역에 값 0x1298CDEF를 저장합니다.");
        System.out.println();
        System.out.println("• read");
        System.out.println("  - 설명: SSD의 특정 LBA 영역에서 데이터를 읽어옵니다.");
        System.out.println("  - 사용법: read [LBA]");
        System.out.println("  - 예시: read 2");
        System.out.println("  - 출력결과: 읽은 값: 0xAAAABBBB");
        System.out.println();
        System.out.println("• fullwrite");
        System.out.println("  - 설명: 모든 LBA 영역(0~99)에 동일한 데이터를 저장합니다.");
        System.out.println("  - 사용법: fullwrite [DATA]");
        System.out.println("  - 예시: fullwrite 0xABCDFFFF");
        System.out.println("  - 설명: 모든 LBA에 값 0xABCDFFFF를 저장합니다.");
        System.out.println();
        System.out.println("• fullread");
        System.out.println("  - 설명: LBA 0번부터 99번까지 모든 데이터를 읽어 출력합니다.");
        System.out.println("  - 사용법: fullread");
        System.out.println("  - 출력결과: 모든 LBA의 값들이 화면에 출력됩니다.");
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