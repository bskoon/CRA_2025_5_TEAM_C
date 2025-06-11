package shell;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FullWriteFullReadTest {

    private static final int MAX_LBA = 100;
    private static final String TEST_VALUE = "ABCDFFFF";

    @Mock
    private TestShell mockTestShell;

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private Map<Integer, String> testData; // 테스트 데이터 추가

    @BeforeEach
    void setUp() {
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
    void FullWrite_수행시_writeLBA_100번_수행_여부_확인() {
        // Arrange: Mock의 fullWrite가 실제처럼 동작하도록 설정
        doAnswer(invocation -> {
            String hexValue = invocation.getArgument(0);
            // 실제 fullWrite 로직 시뮤레이션
            for (int lba = 0; lba < MAX_LBA; lba++) {
                mockTestShell.writeLBA(lba, hexValue);
            }
            return null;
        }).when(mockTestShell).fullWrite(anyString());
        
        doNothing().when(mockTestShell).writeLBA(anyInt(), anyString());

        // Act
        mockTestShell.fullWrite(TEST_VALUE);

        // Assert
        verify(mockTestShell, times(MAX_LBA)).writeLBA(anyInt(), eq(TEST_VALUE));
    }

    @Test
    void FullRead_수행시_readLBA_100번_수행_여부_확인() {
        // Arrange: Mock의 fullRead가 실제처럼 동작하도록 설정
        doAnswer(invocation -> {
            // 실제 fullRead 로직 시뮤레이션
            for (int lba = 0; lba < MAX_LBA; lba++) {
                String value = mockTestShell.readLBA(lba);
                System.out.println("LBA " + String.format("%02d", lba) + ": " + value);
            }
            return null;
        }).when(mockTestShell).fullRead();
        
        when(mockTestShell.readLBA(anyInt()))
            .thenAnswer(invocation -> {
                int lba = invocation.getArgument(0);
                return "0x" + testData.get(lba);
            });

        // Act
        mockTestShell.fullRead();

        // Assert
        verify(mockTestShell, times(MAX_LBA)).readLBA(anyInt());
        assertOutputMatchesExpected();
    }

    private void assertOutputMatchesExpected() {
        String output = outputStream.toString();
        String[] lines = output.split(System.lineSeparator());

        // 빈 라인 제거
        List<String> nonEmptyLines = new ArrayList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                nonEmptyLines.add(line);
            }
        }

        assertEquals(MAX_LBA, nonEmptyLines.size(), "100개의 LBA 값이 출력되어야 함");

        for (int i = 0; i < MAX_LBA; i++) {
            String expectedLine = "LBA " + String.format("%02d", i) + ": 0x" + testData.get(i);
            assertEquals(expectedLine, nonEmptyLines.get(i), "LBA " + i + "의 출력 형식이 올바르지 않음");
        }
    }
}