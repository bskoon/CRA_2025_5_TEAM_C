package shell;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TestShell {
    private static final int MAX_LBA = 100;
    private static final String JAR_FILE_PATH = "SSD.jar";
    private static final List<String> EXECUTE_JAR = new ArrayList<>(List.of("java", "-jar", JAR_FILE_PATH));
    public static final String WRITE = "write";
    public static final String READ = "read";
    public static final String FULLREAD = "fullread";
    public static final String FULLWRITE = "fullwrite";
    public static final String EXIT = "exit";
    public static final String SCRIPT_1 = "1_fullwriteandreadcompare";
    public static final String SCRIPT_2 = "2_partiallbawrite";
    public static final String SCRIPT_3 = "3_writereadaging";


    private TestScenario testScenario;
    private Scanner scanner;
    private boolean isRunning;


    public TestShell() {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        this.testScenario = new TestScenario(this,new Random());
    }

    public static void main(String[] args) {
        TestShell shell = new TestShell();
        shell.launchShell();
    }

    private String getExactCommand(String rawCommand) {
        switch (rawCommand) {
            case "1_":
            case SCRIPT_1:
                return SCRIPT_1;
            case "2_":
            case SCRIPT_2:
                return SCRIPT_2;
            case "3_":
            case SCRIPT_3:
                return SCRIPT_3;
            default:
                return rawCommand;

        }
    }

    public void launchShell() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("SSD Test Shell 시작 (명령어 입력: write/read)");

        while (isRunning) {
            System.out.print("> ");
            String shellCommand = scanner.nextLine().trim();
            String[] commandParameters = shellCommand.split("\\s+");

            if (commandParameters.length == 0) continue;
            String cmd = getExactCommand(commandParameters[0].toLowerCase());

            switch (cmd) {
                case WRITE:
                    if (!isValidParameterCount("write", commandParameters.length) || !isValidLBA(commandParameters[1]) || !isValidValue(commandParameters[2])) {
                        System.out.println("INVALID COMMAND");
                        break;
                    }
                    int readLBA = Integer.parseInt(commandParameters[1]);
                    writeLBA(readLBA, commandParameters[2]);
                    break;

                case READ:
                    if (!isValidParameterCount("read", commandParameters.length) || !isValidLBA(commandParameters[1])) {
                        System.out.println("INVALID COMMAND");
                        break;
                    }
                    int writeLBA = Integer.parseInt(commandParameters[1]);
                    String readVal = readLBA(writeLBA);
                    System.out.println("LBA " + String.format("%02d", writeLBA) + ": " + readVal);
                    break;
                case FULLREAD:
                    if (!isValidParameterCount("fullread", commandParameters.length)) {
                        System.out.println("INVALID COMMAND");
                        break;
                    }
                    fullRead();
                    break;
                case FULLWRITE:
                    if (!isValidParameterCount("fullwrite", commandParameters.length) || !isValidValue(commandParameters[1])) {
                        System.out.println("INVALID COMMAND");
                        break;
                    }
                    fullWrite(commandParameters[1]);
                    break;
                case EXIT:
                    exit();
                    break;
                case "help":
                    help();
                    break;
                case "1_":
                case "1_fullwriteandreadcompare":
                    try {
                        System.out.println(testScenario.fullWriteAndReadCompare());
                    } catch (IOException e){

                    }
                    break;
                case "2_":
                case "2_partiallbawrite":
                    try {
                        System.out.println(testScenario.partialLBAWrite());
                    } catch (IOException e){

                    }
                    break;
                case "3_":
                case "3_writereadaging":
                    try {
                        System.out.println(testScenario.writeReadAging());
                    } catch (IOException e){

                    }
                    break;
                default:
                    System.out.println("INVALID COMMAND");
            }
        }
    }

    private static boolean isValidParameterCount(String command, int parameterCount) {
        int validCount;
        switch (command) {
            case "read":
                validCount = 2;
                break;
            case "write":
                validCount = 3;
                break;
            case "fullread":
                validCount = 1;
                break;
            case "fullwrite":
                validCount = 2;
                break;
            default:
                validCount = 1;
                break;
        }
        return parameterCount == validCount;
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
        callSsdWriteProcess(lba,hexValue);
    }

    public String readLBA(int lba) {
        return callSsdReadProcess(lba);
    }

    private void callSsdWriteProcess(int lba, String hexValue){
        // 실행할 명령어 인자를 설정
        List<String> executableCommand = generateCommand("W", lba, hexValue);
        callSSD(executableCommand);
    }

    private String callSsdReadProcess(int lba) {
        List<String> executableCommand = generateCommand("R", lba, "");
        callSSD(executableCommand);

        return readSSDDataFromOutputFile();  // 결과 반환;
    }

    private static List<String> generateCommand(String type, int lba, String hexValue) {
        List<String> executableCommand = new ArrayList<>(EXECUTE_JAR);

        String lbaString = Integer.toString(lba);

        executableCommand.add(type);
        executableCommand.add(lbaString);
        if (type.equals("W")) executableCommand.add(hexValue);

        return executableCommand;
    }

    private static void callSSD(List<String> executableCommand) {
        // ProcessBuilder 생성
        ProcessBuilder processBuilder = new ProcessBuilder(executableCommand);

        // 프로세스 실행
        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String readSSDDataFromOutputFile() {
        String result = "";
        try {
            // 파일 경로
            String filePath = "ssd_output.txt";
            result = readFileToString(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result.replace("\n","").trim();
    }

    public String readFileToString(String filePath) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append(System.lineSeparator());  // 각 줄마다 줄바꿈 추가
        }

        reader.close();
        return stringBuilder.toString();
    }

    public void fullWrite(String hexValue) {
        for (int lba = 0; lba < MAX_LBA; lba++) {
            writeLBA(lba, hexValue);
        }
        System.out.println("[Write] Done");
    }

    public void fullRead() {
        for (int lba = 0; lba < MAX_LBA; lba++) {
            String value = readLBA(lba);
            System.out.println("LBA " + String.format("%02d", lba) + ": " + value);
        }
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
