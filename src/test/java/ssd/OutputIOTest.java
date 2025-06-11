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

class OutputIOTest {
    @TempDir
    Path tempDir;

    private OutputIO outputIO;
    private SSDIO ssdIO;

    private Path ssdPath;
    private Path outputPath;

    @BeforeEach
    void setUp() {
        outputIO = new OutputIO(SSDConstant.OUTPUT_FILE_PATH);
        ssdIO = new SSDIO(SSDConstant.SSD_FILE_PATH);

        outputPath = Path.of(SSDConstant.OUTPUT_FILE_PATH);
        ssdPath = Path.of(SSDConstant.SSD_FILE_PATH);
    }

    @AfterEach
    void tearDown() {
        deleteFileIfExists(SSDConstant.OUTPUT_FILE_PATH);
        deleteFileIfExists(SSDConstant.SSD_FILE_PATH);
    }

    private void deleteFileIfExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            assertTrue(file.delete(), "파일 삭제 실패: " + path);
        }
    }

    @Test
    void testOutputFileExists() {
        assertTrue(Files.exists(outputPath));
    }

    @Test
    void testSsdFileExists() {
        assertTrue(Files.exists(ssdPath));
    }

    @Test
    void testSsdWriteZero() throws IOException {
        ssdIO.write(0, "0xABCDEF12");

        List<String> lines = Files.readAllLines(ssdPath);
        assertEquals("0xABCDEF12", lines.get(0));
    }

    @Test
    void testSsdWriteFifty() throws IOException {
        ssdIO.write(50, "0xABCDEF12");

        List<String> lines = Files.readAllLines(ssdPath);
        assertEquals("0xABCDEF12", lines.get(50));
    }

    @Test
    void testOutputWriteError() throws IOException {
        outputIO.write(0,"ERROR");

        List<String> lines = Files.readAllLines(outputPath);
        assertEquals("ERROR", lines.get(0));
    }

    @Test
    void testOutputWriteValue() throws IOException {
        outputIO.write(0,"0xABCDEF12");

        List<String> lines = Files.readAllLines(outputPath);
        assertEquals("0xABCDEF12", lines.get(0));
    }
}