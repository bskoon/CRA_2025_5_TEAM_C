package shell;

import shell.command.*;
import shell.util.Logger;
import shell.util.Runner;
import shell.util.Utility;

import java.util.*;

import static shell.util.ShellConstant.*;

public class TestShell {
    private static final Logger log = Logger.getLogger();
    private static final Utility util = Utility.getInstance();

    private Scanner scanner;
    private boolean isRunning;

    private CommandExecutor executor;

    public TestShell(CommandExecutor executor) {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        this.executor = executor;
    }

    public boolean isRunning() {
        return isRunning;
    }

    private void launchShell() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("SSD Test Shell 시작 (명령어 입력: write/read)");
        log.log("TestShell.launchShell()", "Start Shell");

        while (isRunning) {
            String[] commandParameters = getCommandFromShell(scanner);
            if (isInputEmpty(commandParameters)) continue;
            executeCommand(commandParameters);
        }
    }

    private String[] getCommandFromShell(Scanner scanner) {
        System.out.print("> ");
        String shellCommand = scanner.nextLine().trim();
        String[] commandParameters = shellCommand.split("\\s+");
        log.log("TestShell.launchShell()", "Command - " + shellCommand);
        return commandParameters;
    }

    private boolean isInputEmpty(String[] commandParameters) {
        return commandParameters.length == 0 || commandParameters[0].trim().isEmpty();
    }

    private void executeCommand(String[] commandParameters) {
        log.log("TestShell.executeCommand()", "Command Execute Start");

        if (commandParameters[0].equals(EXIT)) exit();
        else if (commandParameters[0].equals(HELP)) help();
        else {
            executor.executeCommand(commandParameters);
        }
    }

    private void exit() {
        System.out.println("Exiting TestShell...");
        log.log("TestShell.exit()", "Shell Close");
        isRunning = false;
        if (scanner != null) {
            scanner.close();
        }
    }

    private void help() {
        log.log("TestShell.help()", "Print help");
        System.out.println("==========================================");
        System.out.println("TestShell Help");
        System.out.println("==========================================");
        System.out.println("제작자: TEAM_C");
        System.out.println("김범석 bskoon");
        System.out.println("김상윤 huihihet");
        System.out.println("김창지 Chang-ji");
        System.out.println("김태엽 taeyeob-kim-09");
        System.out.println("노웅규 nohog94");
        System.out.println("최재형 jae637");
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
        System.out.println("• erase");
        System.out.println("  - 설명: SSD의 특정 LBA부터 SIZE만큼 데이터를 지웁니다.");
        System.out.println("  - 사용법: erase [LBA] [SIZE]");
        System.out.println("  - 예시: erase 5 24");
        System.out.println();
        System.out.println("• erase_range");
        System.out.println("  - 설명: SSD의 START_LBA부터 END_LBA까지 데이터를 지웁니다..");
        System.out.println("  - 사용법: erase_range [START_LBA] [END_LBA]");
        System.out.println("  - 예시: erase_range 3 19");
        System.out.println();
        System.out.println("• flush");
        System.out.println("  - 설명: SSD Buffer에 임시 기록중인 데이터를 모두 SSD에 반영합니다.");
        System.out.println("  - 사용법: flush");
        System.out.println();
        System.out.println("• exit");
        System.out.println("  - 설명: TestShell을 종료합니다.");
        System.out.println();
        System.out.println("• help");
        System.out.println("  - 설명: 이 도움말을 표시합니다.");
        System.out.println("==========================================");
    }

    public static void main(String[] args) {
        try {
            CommandExecutor executor = util.getCommandExecutor();
            if (args.length != 0)
                new Runner(args, executor).run();
            else {
                TestShell shell = new TestShell(executor);
                shell.launchShell();
            }
        } catch (Exception e) {
            log.log("TestShell.main()", "Shell Start Fail caused by Exception");
        }
    }
}
