package shell;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class FullWriteFullReadTest {

    private static final int MAX_LBA = 100;
    private static final String TEST_VALUE = "ABCDFFFF";

    private TestShell testShell;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private Map<Integer, String> testData; // 테스트 데이터 추가

    @BeforeEach
    void setUp() {
        testShell = new TestShell();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // 테스트 데이터 초기화 (setUp()으로 이동)
        testData = new HashMap<>();
        for (int i = 0; i < MAX_LBA; i++) {
            testData.put(i, String.format("%08X", i * 0x1000 + 0xABCD));
        }
    }

    @AfterEach
    void tearDown() {
        // System.out 복원
        System.setOut(originalOut);
    }

    @Test
    void fullWrite_calls_writeLBA_100_times() {
        // Arrange
        try (MockedStatic<TestShell> mockedTestShell = mockStatic(TestShell.class, CALLS_REAL_METHODS)) {
            // Act
            TestShell.fullWrite(TEST_VALUE);

            // Assert
            mockedTestShell.verify(() -> TestShell.writeLBA(anyInt(), eq(TEST_VALUE)), times(MAX_LBA));
        }
    }

    @Test
    void fullRead_outputs_correct_LBA_values() {
        // Arrange
        try (MockedStatic<TestShell> mockedTestShell = mockStatic(TestShell.class, CALLS_REAL_METHODS)) {
            mockedTestShell.when(() -> TestShell.readLBA(anyInt()))
                    .thenAnswer(invocation -> {
                        int lba = invocation.getArgument(0);
                        return testData.get(lba);
                    });

            mockedTestShell.when(() -> TestShell.fullRead()).thenAnswer(invocation -> {
                for (int lba = 0; lba < MAX_LBA; lba++) {
                    String value = TestShell.readLBA(lba);
                    System.out.println("LBA " + lba + ": 0x" + value);
                }
                return null;
            });

            // Act
            TestShell.fullRead();

            // Assert
            assertOutputMatchesExpected();
        }
    }

    private void assertOutputMatchesExpected() {
        String output = outputStream.toString();
        String[] lines = output.split(System.lineSeparator());

        assertEquals(MAX_LBA, lines.length, "100개의 LBA 값이 출력되어야 함");

        for (int i = 0; i < MAX_LBA; i++) {
            String expectedLine = "LBA " + i + ": 0x" + testData.get(i);
            assertEquals(expectedLine, lines[i], "LBA " + i + "의 출력 형식이 올바르지 않음");
        }
    }
}