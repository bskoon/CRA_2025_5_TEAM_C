package shell;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shell.command.Document;
import shell.command.WriteCommand;
import shell.command.ReadCommand;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestShell_full {

    private static final int MAX_BLOCK_SIZE = 100;
    private static final String TEST_VALUE = "0xABCDFFFF";

    @Mock
    private TestShell mockTestShell;

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private Map<Integer, String> testData; // 테스트 데이터 추가
    private Document mockDocument;
    private WriteCommand writeCommand;
    private ReadCommand readCommand;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        mockDocument = mock(Document.class);
        writeCommand = new WriteCommand(mockDocument);
        readCommand = new ReadCommand(mockDocument);
    }

    @AfterEach
    void tearDown() {
        // System.out 복원
        System.setOut(originalOut);
    }

    @Test
    void FullWrite_정상_테스트_확인() {

        String[] args = {"fullwrite", TEST_VALUE};

        // When
        writeCommand.execute(args);

        // Then
        verify(mockDocument).write(0, MAX_BLOCK_SIZE,TEST_VALUE );

    }

    @Test
    void FullWrite_Input_value_오류_확인() {

        String[] args = {"fullwrite", "ABCDFFFF"};

        // When
        writeCommand.execute(args);

        // Then
        // Then
        String output = outputStream.toString().trim();
        assertTrue(output.contains("INVALID COMMAND"));
        verify(mockDocument, never()).write(0, MAX_BLOCK_SIZE,TEST_VALUE );
    }

    @Test
    void FullRead_정상_테스트_확인() {
        // When
        String[] readArgs = {"fullread"};

        // When
        readCommand.execute(readArgs);

        // Then
        verify(mockDocument).read(0, MAX_BLOCK_SIZE );

    }
}