package shell;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shell.command.Document;
import shell.command.ReadCommand;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestShell_read {
    private static final int MAX_BLOCK_SIZE = 100;

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private Map<Integer, String> testData; // 테스트 데이터 추가
    private Document mockDocument;
    private ReadCommand readCommand;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        mockDocument = mock(Document.class);
        readCommand = new ReadCommand(mockDocument);
    }

    @AfterEach
    void tearDown() {
        // System.out 복원
        System.setOut(originalOut);
    }

    @Test
    void Read_정상_테스트_확인() {       

        String[] readArgs = {"read", "3"};

        // When
        readCommand.execute(readArgs);

        // Then
        verify(mockDocument).read(3, 1 );

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
