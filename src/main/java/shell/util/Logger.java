package shell.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static Logger instance;

    private static final String LOG_FOLDER = "log";
    private static final String LOG_FILE = LOG_FOLDER + "\\latest.txt";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd HH:mm");

    public static Logger getLogger(){
        if (instance == null) instance = new Logger();
        return instance;
    }

    private Logger() {
        File logDir = new File(LOG_FOLDER);
        if (!logDir.exists())
            logDir.mkdirs();
    }

    public static void log(String classMethodName, String message) {
        checkLogFileSize();
        File logFile = new File(LOG_FILE);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                System.err.println("로그 파일 생성 실패: " + e.getMessage());
                return;
            }
        }

        String timestamp = sdf.format(new Date());
        String fixedMethodName = formatMethodName(classMethodName);

        String logLine = String.format("[%s] %s : %s", timestamp, fixedMethodName, message);

        System.out.println(logLine);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logLine);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("로그 저장 실패: " + e.getMessage());
        }
    }

    private static void checkLogFileSize() {
    }

    private static String formatMethodName(String input) {
        int width = 30;
        if (input.length() >= width) {
            return input.substring(0, width);
        } else {
            return String.format("%-" + width + "s", input);  // 왼쪽 정렬 + 공백 채움
        }
    }

    public static void main(String[] args) {
        log("TestShell.launchShell()", "Shell Start1");
    }
}
