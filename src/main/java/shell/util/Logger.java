package shell.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class Logger {
    private static Logger instance;

    private static final String LOG_FOLDER = "log";
    private static final String LOG_FILE = LOG_FOLDER + "\\latest.txt";
    private static final long MAX_LOG_SIZE = 10 * 1024;

    private static final SimpleDateFormat logDateFormat = new SimpleDateFormat("yy.MM.dd HH:mm");
    private static final SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyMMdd_HH'h'_mm'm'_ss's'");

    public static Logger getLogger() {
        if (instance == null) instance = new Logger();
        return instance;
    }

    private Logger() {
        File logDir = new File(LOG_FOLDER);
        if (!logDir.exists()) logDir.mkdirs();
    }

    public void log(String classMethodName, String message) {
        checkLogSizeAndRoll();
        checkOldLog2Zip();
        writeData2LogFile(getLogString(classMethodName, message));
    }

    private static void checkLogSizeAndRoll() {
        File logFile = new File(LOG_FILE);
        if (logFile.exists() && logFile.length() > MAX_LOG_SIZE) {
            String timeStr = fileDateFormat.format(new Date());
            String rolledName = LOG_FOLDER + "/until_" + timeStr + ".log";
            File rolledFile = new File(rolledName);
            logFile.renameTo(rolledFile);
        }
    }

    private static void checkOldLog2Zip() {
        File[] rolledLogs = new File(LOG_FOLDER).listFiles((dir, name) -> name.endsWith(".log"));
        if (rolledLogs != null && rolledLogs.length >= 2) {
            Arrays.sort(rolledLogs, Comparator.comparingLong(File::lastModified));

            for (int i = 0; i < rolledLogs.length - 1; i++) {
                File f = rolledLogs[i];
                File zipped = new File(f.getAbsolutePath().replace(".log", ".zip"));
                f.renameTo(zipped);
            }
        }
    }

    private static void writeData2LogFile(String logLine) {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            br.write(logLine);
            br.newLine();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private static String getLogString(String classMethodName, String message) {
        String timestamp = logDateFormat.format(new Date());
        String fixedMethodName = formatMethodName(classMethodName);
        return String.format("[%s] %s : %s", timestamp, fixedMethodName, message);
    }

    private static String formatMethodName(String input) {
        int width = 50;
        if (input.length() >= width) {
            return input.substring(0, width);
        } else {
            return String.format("%-" + width + "s", input);  // 왼쪽 정렬 + 공백 채움
        }
    }
}
