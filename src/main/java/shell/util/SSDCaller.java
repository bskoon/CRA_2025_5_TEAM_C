package shell.util;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SSDCaller {
    private static SSDCaller instance;

    private static final String JAR_FILE_PATH = "SSD.jar";
    private static final String OUTPUT_FILE = "ssd_output.txt";

    private static final List<String> EXECUTE_COMMAND = new ArrayList<>(List.of("java", "-jar", JAR_FILE_PATH));

    public static SSDCaller getInstance() {
        if (instance == null) instance = new SSDCaller();
        return instance;
    }

    public void callSSD(String... args) {
        List<String> executableCommand = generateExecutableCommand(args);
        ProcessBuilder processBuilder = new ProcessBuilder(executableCommand);

        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> generateExecutableCommand(String... args) {
        List<String> baseExecuteCommand = new ArrayList<>(EXECUTE_COMMAND);

        baseExecuteCommand.addAll(Arrays.asList(args));

        return baseExecuteCommand;
    }

    public String getReadOutput() {
        String result = readRawSSDOutputData();

        return result.replace("\n","").trim();
    }

    private String readRawSSDOutputData() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(OUTPUT_FILE))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator());  // 각 줄마다 줄바꿈 추가
            }

            return stringBuilder.toString();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
