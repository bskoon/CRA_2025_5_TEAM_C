package shell.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {

    public static final Logger log = Logger.getLogger();
    private static final String LOG_FOLDER = "log";
    private static final String LOG_FILE_PATH = LOG_FOLDER + "\\latest.txt";
    private static File LOG_DIR;
    private static File LOG_FILE;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd HH:mm");

    @BeforeEach
    void setUp() {
        LOG_DIR = new File(LOG_FOLDER);
        LOG_FILE = new File(LOG_FILE_PATH);
    }

    @AfterEach
    void tearDown() {
        File logDir = new File(LOG_FOLDER); // 예: "logs"
        if (logDir.exists() && logDir.isDirectory()) {
            for (File f : logDir.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                }
            }
        }
    }

    @Test
    void Logger_log폴더_확인() {
        assertTrue(Files.exists(LOG_DIR.toPath()));
    }

    @Test
    void Logger_log함수_호출() throws IOException {
        String timestamp = sdf.format(new Date());
        String expected = "[" + timestamp + "] TestShell.launchShell()        : Shell Start";

        log.log("TestShell.launchShell()", "Shell Start");

        List<String> lines = Files.readAllLines(LOG_FILE.toPath());
        assertEquals(expected, lines.get(0));
    }
}