package shell.util;

import shell.command.CommandExecutor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.List;

public class Runner {
    private static final Logger log = Logger.getLogger();

    private File SCRIPT_FILE;
    private final CommandExecutor executor;

    public Runner(String[] args, CommandExecutor executor) {
        SCRIPT_FILE = new File(args[0]);
        log.log("Runner.Runner()", "Check Script Name - " + args[0]);
        validCheck();

        this.executor = executor;
        Logger.getLogger().setRunner(true);
    }

    private void validCheck() {
        if (!SCRIPT_FILE.exists()){
            log.log("Runner.validCheck()", "Invalid File Name");
            throw new RuntimeException("Invalid File Name");
        }
    }

    public void run() {
        List<String> scenarioList = readScriptFile();
        if (scenarioList == null) return;

        executeEachScenario(scenarioList);
    }

    private List<String> readScriptFile() {
        List<String> scenarioList;
        log.log("Runner.readScenarioFile()", "Load Script File");
        try {
            scenarioList = Files.readAllLines(SCRIPT_FILE.toPath());
        } catch (Exception e) {
            log.log("Runner.readScenarioFile()", "Exception occur while read Runner file");
            return null;
        }
        return scenarioList;
    }

    private void executeEachScenario(List<String> scenarioList) {
        for (String s : scenarioList) {
            System.out.print(s + "   ___   Run...");
            log.log("Runner.executeEachScenario()", "Run Script - " + s);
            System.out.println(getPassFail(s));
        }
    }

    private String getPassFail(String ScenarioName) {
        ByteArrayOutputStream outContent = executeScenarioWithNoSysOut(ScenarioName);
        String outString = outContent.toString().replace("\r\n", "");
        log.log("Runner.getPassFail()", "Script Result : " + outString);
        return outString;
    }

    private ByteArrayOutputStream executeScenarioWithNoSysOut(String ScenarioName) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        executor.executeCommand(new String[]{ScenarioName});

        System.setOut(originalOut);
        return outContent;
    }
}
