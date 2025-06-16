package ssd;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ssd.IO.OutputIO;
import ssd.IO.SSDIO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ssd.common.SSDConstant.*;

class OutputIOTest {
    @TempDir
    Path tempDir;

    private OutputIO outputIO;
    private SSDIO ssdIO;

    private Path ssdPath;
    private Path outputPath;

    @BeforeEach
    void setUp() {
        outputIO = new OutputIO(OUTPUT_FILE_PATH);
        ssdIO = new SSDIO(SSD_FILE_PATH);

        outputPath = Path.of(OUTPUT_FILE_PATH);
        ssdPath = Path.of(SSD_FILE_PATH);
    }

    @AfterEach
    void tearDown() {
        deleteFileIfExists(OUTPUT_FILE_PATH);
        deleteFileIfExists(SSD_FILE_PATH);
    }

    private void deleteFileIfExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            assertTrue(file.delete(), "파일 삭제 실패: " + path);
        }
    }

    @Test
    void outputFile_생성_확인() {
        assertTrue(Files.exists(outputPath));
    }

    @Test
    void ssdFile_생성_확인() {
        assertTrue(Files.exists(ssdPath));
    }

    @Test
    void ssd_0번에_쓰기_정상_확인() throws IOException {
        ssdIO.write(0, "0xABCDEF12");

        assertFileLineEquals(ssdPath, "0 0xABCDEF12", 0);
    }

    @Test
    void ssd_50번에_쓰기_정상_확인() throws IOException {
        ssdIO.write(50, "0xABCDEF12");

        assertFileLineEquals(ssdPath, "50 0xABCDEF12", 50);
    }

    @Test
    void output_0번에_ERROR_쓰기_확인() throws IOException {
        outputIO.write(0,"ERROR");

        assertFileLineEquals(outputPath, "ERROR", 0);
    }

    @Test
    void output_0번에_VALUE_쓰기_확인() throws IOException {
        outputIO.write(0,"0xABCDEF12");

        assertFileLineEquals(outputPath, "0xABCDEF12", 0);
    }

    private void assertFileLineEquals(Path path, String value, int index) throws IOException {
        List<String> lines = Files.readAllLines(path);
        assertEquals(value, lines.get(index));
    }
}