import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestShellTest {
    private final Path nandPath = Paths.get("ssd_nand.txt");
    private final Path outputPath = Paths.get("ssd_output.txt");

    @BeforeEach
    void setup() throws IOException {
        Files.deleteIfExists(nandPath);
        Files.deleteIfExists(outputPath);
    }

    @Test
    void testWriteAndReadSameLBA() throws IOException {
        SSD.main(new String[]{"W", "5", "0xA1B2C3D4"});
        SSD.main(new String[]{"R", "5"});
        String output = Files.readString(outputPath).trim();
        assertEquals("0xA1B2C3D4", output);
    }

    @Test
    void testReadUnwrittenLBA() throws IOException {
        SSD.main(new String[]{"R", "3"});
        String output = Files.readString(outputPath).trim();
        assertEquals("0x00000000", output);
    }

    @Test
    void testInvalidLBAForWrite() throws IOException {
        SSD.main(new String[]{"W", "150", "0x12345678"});
        String output = Files.readString(outputPath).trim();
        assertEquals("ERROR", output);
    }

    @Test
    void testInvalidValueFormat() throws IOException {
        SSD.main(new String[]{"W", "10", "12345678"});
        String output = Files.readString(outputPath).trim();
        assertEquals("ERROR", output);
    }

    @Test
    void testInvalidCommand() throws IOException {
        SSD.main(new String[]{"Z", "1", "0x12345678"});
        String output = Files.readString(outputPath).trim();
        assertEquals("ERROR", output);
    }

    @Test
    void testMockedNandStorage() throws IOException {
        // Create a mock NAND array with test data
        String[] mockNand = new String[100];
        for (int i = 0; i < 100; i++) {
            mockNand[i] = String.format("%08X", i);
        }

        // Use mock static for loadNand method
        try (MockedStatic<SSD> mockedSSD = mockStatic(SSD.class, CALLS_REAL_METHODS)) {
            mockedSSD.when(() -> SSD.main(new String[]{"R", "10"})).thenCallRealMethod();
            mockedSSD.when(() -> SSD.readLBA(10)).thenReturn(mockNand[10]);
            mockedSSD.when(() -> SSD.loadNand()).thenReturn(mockNand);

            SSD.main(new String[]{"R", "10"});
            String output = Files.readString(outputPath).trim();
            assertEquals("0x0000000A", output);  // 10 in hex
        }
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(nandPath);
        Files.deleteIfExists(outputPath);
    }
}
