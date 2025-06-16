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

    private boolean isRunner = false;

    public static Logger getLogger() {
        if (instance == null) instance = new Logger();
        return instance;
    }

    private Logger() {
        File logDir = new File(LOG_FOLDER);
        if (!logDir.exists()) logDir.mkdirs();
    }

    public void setRunner(boolean runner) {
        isRunner = runner;
    }

    public void print(String message) {
        if (!isRunner) System.out.println(message);
    }

    public void log(String classMethodName, String message) {
        rollLogIfSizeOver();
        checkOldLog2Zip();

        String logMessage = getLogString(classMethodName, message);
        writeData2LogFile(logMessage);
    }

    private void rollLogIfSizeOver() {
        File logFile = new File(LOG_FILE);
        if (isLogSizeOver(logFile)) {
            String rolledName = setRolledLogFileName();
            logFile.renameTo(new File(rolledName));
        }
    }

    private boolean isLogSizeOver(File logFile) {
        return logFile.exists() && logFile.length() > MAX_LOG_SIZE;
    }

    private String setRolledLogFileName() {
        String timeStr = fileDateFormat.format(new Date());
        String rolledName = LOG_FOLDER + "/until_" + timeStr + ".log";
        return rolledName;
    }

    private void checkOldLog2Zip() {
        File[] logList = new File(LOG_FOLDER).listFiles((dir, name)
                                                            -> name.endsWith(".log"));
        if (hasOldLog(logList)) {
            zipOldLogs(logList);
        }
    }

    private boolean hasOldLog(File[] rolledLogs) {
        return rolledLogs != null && rolledLogs.length >= 2;
    }

    private void zipOldLogs(File[] rolledLogs) {
        Arrays.sort(rolledLogs, Comparator.comparingLong(File::lastModified));

        for (int i = 0; i < rolledLogs.length - 1; i++) {
            File f = rolledLogs[i];
            File zipped = new File(f.getAbsolutePath().replace(".log", ".zip"));
            f.renameTo(zipped);
        }
    }

    private void writeData2LogFile(String logLine) {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            br.write(logLine);
            br.newLine();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private String getLogString(String classMethodName, String message) {
        String timestamp = logDateFormat.format(new Date());
        String fixedMethodName = formatMethodName(classMethodName);
        return String.format("[%s] %s : %s", timestamp, fixedMethodName, message);
    }

    private String formatMethodName(String input) {
        int width = 40;
        if (input.length() >= width) {
            return input.substring(0, width);
        } else {
            return String.format("%-" + width + "s", input);  // 왼쪽 정렬 + 공백 채움
        }
    }
}
