package shell;

import shell.command.*;
import shell.util.Runner;

import java.util.*;

import static shell.util.ShellConstant.*;

public class TestShell {
    private Scanner scanner;
    private boolean isRunning;

    private CommandExecutor executor;

    public TestShell() {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        executor = initCommandExecutor();
    }

    public static CommandExecutor initCommandExecutor() {
        Document document = new Document();
        Command readCommand = new ReadCommand(document);
        Command writeCommand = new WriteCommand(document);
        Command eraseCommand = new EraseCommand(document);
        Command flushCommand = new FlushCommand(document);
        Command scenarioCommand = new ScenarioCommand(document);

        CommandExecutor executor = new CommandExecutor();
        executor.setCommand(READ, readCommand);
        executor.setCommand(WRITE, writeCommand);
        executor.setCommand(ERASE, eraseCommand);
        executor.setCommand(FULLREAD, readCommand);
        executor.setCommand(FULLWRITE, writeCommand);
        executor.setCommand(ERASERANGE, eraseCommand);
        executor.setCommand(FLUSH, flushCommand);
        executor.setCommand(SCENARIO_1, scenarioCommand);
        executor.setCommand(SCENARIO_2, scenarioCommand);
        executor.setCommand(SCENARIO_3, scenarioCommand);

        return executor;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    private void launchShell() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("SSD Test Shell 시작 (명령어 입력: write/read)");

        while (isRunning) {
            System.out.print("> ");
            String shellCommand = scanner.nextLine().trim();
            String[] commandParameters = shellCommand.split("\\s+");

            if (commandParameters.length == 0) continue;

            executeCommand(commandParameters);
        }
    }

    private void executeCommand(String[] commandParameters) {
        if (commandParameters[0].equals(EXIT)) {
            exit();
        } else if (commandParameters[0].equals(HELP)) {
            help();
        } else {
            executor.executeCommand(commandParameters);
        }
    }

    private void exit() {
        System.out.println("Exiting TestShell...");
        isRunning = false;
        if (scanner != null) {
            scanner.close();
        }
    }

    private void help() {
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

    public static void main(String[] args) {
        if (args != null)
            new Runner(args, initCommandExecutor()).run();
        else {
            TestShell shell = new TestShell();
            shell.launchShell();
        }
    }
}
