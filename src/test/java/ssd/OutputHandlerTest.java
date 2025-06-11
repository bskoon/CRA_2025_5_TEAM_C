package ssd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OutputHandlerTest {
    @TempDir
    Path tempDir;

    private Path nandFile;
    private Path outputFile;
    private OutputHandler handler;

    @BeforeEach
    void setUp() throws Exception {
        nandFile = tempDir.resolve("ssd_nand.txt");
        outputFile = tempDir.resolve("ssd_output.txt");

        // 리플렉션으로 static 경로 변경
        setStaticField(OutputHandler.class, "NAND_FILE_PATH", nandFile.toString());
        setStaticField(OutputHandler.class, "OUTPUT_FILE_PATH", outputFile.toString());

        handler = new OutputHandler();
    }

    private void setStaticField(Class<?> clazz, String fieldName, String value) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
    }

    @Test
    void testNandWrite_CreatesAndAppends() throws IOException {
        handler.nandWrite("0xABCDEF12");

        assertTrue(Files.exists(nandFile));
        List<String> lines = Files.readAllLines(nandFile);
        assertEquals(1, lines.size());
        assertEquals("0xABCDEF12", lines.get(0));
    }

    @Test
    void testOutputWrite_CreatesAndOverwrites() throws IOException {
        handler.outPutWrite("ERROR");

        assertTrue(Files.exists(outputFile));
        List<String> lines = Files.readAllLines(outputFile);
        assertEquals(1, lines.size());
        assertEquals("ERROR", lines.get(0));
    }
}