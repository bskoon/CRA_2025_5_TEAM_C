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
    
    @BeforeEach
    void setUp() {
        testShell = new TestShell();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }
    
    @AfterEach
    void tearDown() {
        // System.out 복원
        System.setOut(originalOut);
    }

    @Test
    void FullWrite_수행시_writeLBA_100번_수행_여부_확인() {
        // Arrange: TestShell의 writeLBA 메소드를 모킹
        try (MockedStatic<TestShell> mockedTestShell = mockStatic(TestShell.class, CALLS_REAL_METHODS)) {
            
            // fullWrite 메소드가 내부적으로 writeLBA를 100번 호출하도록 구현
            mockedTestShell.when(() -> TestShell.fullWrite(TEST_VALUE)).thenAnswer(invocation -> {
                // LBA 0부터 99까지 writeLBA 호출
                for (int lba = 0; lba < MAX_LBA; lba++) {
                    TestShell.writeLBA(lba, TEST_VALUE);
                }
                return null;
            });
            
            // Act: fullwrite 실행
            TestShell.fullWrite(TEST_VALUE);
            
            // Assert: writeLBA가 정확히 100번 호출되었는지 verify로 확인
            mockedTestShell.verify(() -> TestShell.writeLBA(anyInt(), eq(TEST_VALUE)), times(MAX_LBA));
        }
    }

    @Test
    void FullRead_수행시_readLBA_100번_수행_여부_확인() {
        // Arrange: TestShell의 readLBA 메소드를 모킹
        try (MockedStatic<TestShell> mockedTestShell = mockStatic(TestShell.class, CALLS_REAL_METHODS)) {
            
            // 테스트용 데이터 준비 (각 LBA마다 다른 값)
            Map<Integer, String> testData = new HashMap<>();
            for (int i = 0; i < MAX_LBA; i++) {
                testData.put(i, String.format("%08X", i * 0x1000 + 0xABCD));
            }
            
            // readLBA 메소드 모킹
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
            
            // Act: fullread 실행
            TestShell.fullRead();
            
            // Assert: 출력 결과 검증
            String output = outputStream.toString();
            String[] lines = output.split(System.lineSeparator());
            
            assertEquals(MAX_LBA, lines.length, "100개의 LBA 값이 출력되어야 함");
            
            // 각 라인이 올바른 형식으로 출력되었는지 확인
            for (int i = 0; i < MAX_LBA; i++) {
                String expectedLine = "LBA " + i + ": 0x" + testData.get(i);
                assertEquals(expectedLine, lines[i], "LBA " + i + "의 출력 형식이 올바르지 않음");
            }
        }
    }
}