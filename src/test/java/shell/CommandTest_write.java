package shell;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import shell.command.Document;
import shell.command.WriteCommand;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandTest_write {

    private static final int MAX_BLOCK_SIZE = 100;
    private static final String TEST_VALUE = "0xABCDFFFF";


    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private Map<Integer, String> testData; // 테스트 데이터 추가
    private Document mockDocument;
    private WriteCommand writeCommand;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        mockDocument = mock(Document.class);
        writeCommand = new WriteCommand(mockDocument);
    }

    @AfterEach
    void tearDown() {
        // System.out 복원
        System.setOut(originalOut);
    }

    @Test
    void Write_정상_테스트_확인() {

        String[] args = {"write", "3", TEST_VALUE};

        // When
        writeCommand.execute(args);

        // Then
        verify(mockDocument).write(3, 1, TEST_VALUE);

    }

    @Test
    void Write_LBA_오류_확인() {

        String[] args = {"write", "-1", "ABCDFFFF"};

        // When
        writeCommand.execute(args);

        // Then
        // Then
        String output = outputStream.toString().trim();
        assertTrue(output.contains("INVALID COMMAND"));
        verify(mockDocument, never()).write(3, 1,TEST_VALUE );
    }

    @Test
    void Write_Input_value_오류_확인() {

        String[] args = {"write", "3", "ABCDFFFF"};

        // When
        writeCommand.execute(args);

        // Then
        String output = outputStream.toString().trim();
        assertTrue(output.contains("INVALID COMMAND"));
        verify(mockDocument, never()).write(3, 1, TEST_VALUE);
    }

    @Test
    void FullWrite_정상_테스트_확인() {

        String[] args = {"fullwrite", TEST_VALUE};

        // When
        writeCommand.execute(args);

        // Then
        verify(mockDocument).write(0, MAX_BLOCK_SIZE, TEST_VALUE);

    }

    @Test
    void FullWrite_Input_value_오류_확인() {

        String[] args = {"fullwrite", "ABCDFFFF"};

        // When
        writeCommand.execute(args);

        // Then
        String output = outputStream.toString().trim();
        assertTrue(output.contains("INVALID COMMAND"));
        verify(mockDocument, never()).write(0, MAX_BLOCK_SIZE, TEST_VALUE);
    }
}
