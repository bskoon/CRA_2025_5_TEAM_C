package shell.util;

import shell.command.CommandExecutor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.List;

import static shell.TestShell.initCommandExecutor;

public class Runner {
    private File SCRIPT_FILE;

    private final CommandExecutor executor;

    public Runner(String[] args, CommandExecutor executor) {
        SCRIPT_FILE = new File(args[0]);
        validCheck();

        this.executor = executor;
        Logger.getLogger().setRunner(true);
    }

    private void validCheck() {
        if (!SCRIPT_FILE.exists())
            throw new RuntimeException();

    }

    public void run() {
        List<String> scenarioList;
        try {
            scenarioList = Files.readAllLines(SCRIPT_FILE.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (String s : scenarioList) {
            System.out.print(s + "   ___   Run...");
            System.out.println(getPassFail(s).equals("PASS") ? "Pass" : "FAIL!");
        }
    }

    private String getPassFail(String ScenarioName) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        executor.executeCommand(new String[]{ScenarioName});

        System.setOut(originalOut);
        return outContent.toString().replace("\r\n", "");
    }

    public static void main(String[] args) {
        new Runner(new String[]{"shell_scripts.txt"}, initCommandExecutor()).run();
    }
}
