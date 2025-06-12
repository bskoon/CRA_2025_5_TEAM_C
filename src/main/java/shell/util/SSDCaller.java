package shell.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SSDCaller {
    private static SSDCaller instance;

    private static final String JAR_FILE_PATH = "SSD.jar";
    private static final String OUTPUT_FILE = "ssd_output.txt";

    private static final List<String> EXECUTE_COMMAND = new ArrayList<>(List.of("java", "-jar", JAR_FILE_PATH));

    public static SSDCaller getSSDCaller() {
        if (instance == null) instance = new SSDCaller();
        return instance;
    }

    public String readOnSSD(int lba) {
        List<String> readArgument = new ArrayList<>();
        readArgument.add("R");
        readArgument.add(Integer.toString(lba));

        callSSD(readArgument);

        return readSSDDataFromOutputFile();
    }

    public void writeOnSSD(int lba, String hexValue) {
        List<String> writeArgument = new ArrayList<>();
        writeArgument.add("W");
        writeArgument.add(Integer.toString(lba));
        writeArgument.add(hexValue);

        callSSD(writeArgument);
    }

    public void eraseOnSSD(int lba, int size) {
        List<String> eraseArgument = new ArrayList<>();
        eraseArgument.add("E");
        eraseArgument.add(Integer.toString(lba));
        eraseArgument.add(Integer.toString(size));

        callSSD(eraseArgument);
    }

    private String readSSDDataFromOutputFile() {
        String result = "";
        try {
            result = readFileToString(OUTPUT_FILE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result.replace("\n","").trim();
    }

    private String readFileToString(String filePath) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append(System.lineSeparator());  // 각 줄마다 줄바꿈 추가
        }

        reader.close();
        return stringBuilder.toString();
    }

    private List<String> generateExecutableCommand(List<String> args) {
        List<String> executableCommand = new ArrayList<>(EXECUTE_COMMAND);
        executableCommand.addAll(args);

        return executableCommand;
    }

    private void callSSD(List<String> args) {
        List<String> executableCommand = generateExecutableCommand(args);

        ProcessBuilder processBuilder = new ProcessBuilder(executableCommand);

        // 프로세스 실행
        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
