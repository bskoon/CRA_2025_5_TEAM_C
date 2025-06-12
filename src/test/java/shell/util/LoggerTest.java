package shell.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoggerTest {

    public static final Logger log = Logger.getLogger();
    private static File LOG_DIR;
    private static File LOG_FILE;

    private static final long MAX_LOG_SIZE = 10 * 1024;
    private static final SimpleDateFormat logDateFormat = new SimpleDateFormat("yy.MM.dd HH:mm");

    @BeforeEach
    void setUp() {
        LOG_DIR = new File("log");
        LOG_FILE = new File("log\\latest.txt");
    }

    @AfterEach
    void tearDown() {
        if (LOG_DIR.exists() && LOG_DIR.isDirectory()) {
            for (File f : LOG_DIR.listFiles()) f.delete();
        }
    }

    @Test
    void Logger_log폴더_확인() {
        assertTrue(Files.exists(LOG_DIR.toPath()));
    }

    @Test
    void Logger_log함수_호출시_latest생성() throws IOException {
        String timestamp = logDateFormat.format(new Date());
        String expected = "[" + timestamp + "] TestShell.launchShell()        : Shell Start";

        log.log("TestShell.launchShell()", "Shell Start");

        List<String> lines = Files.readAllLines(LOG_FILE.toPath());
        assertEquals(expected, lines.get(0));
    }

    @Test
    void Logger_10KB_이상시_Roll() {
        logging200Line();

        File[] logFiles = LOG_DIR.listFiles((dir, name) -> name.endsWith(".log"));
        assertTrue(logFiles != null && logFiles.length > 0);
    }

    @Test
    void Logger_Roll_파일_10KB_이상인지() {
        logging200Line();

        File[] logFiles = LOG_DIR.listFiles((dir, name) -> name.endsWith(".log"));
        assertTrue(logFiles[0].length() > MAX_LOG_SIZE);
    }

    @Test
    void Logger_log파일_2개_이상시_ZIP() throws InterruptedException {
        logging200Line();

        Thread.sleep(1000);

        logging200Line();

        File[] zipFiles = LOG_DIR.listFiles((dir, name) -> name.endsWith(".zip"));
        assertTrue(zipFiles != null && zipFiles.length > 0);
    }

    private static void logging200Line() {
        for (int i = 0; i < 200; i++)
            log.log("TestShell.launchShell()", "Shell Start");
    }
}