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
public class TestShellTest {

    private static final int MAX_LBA = 99;
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
    void Write_정상_테스트_확인() {

        String[] args = {"write", "3", TEST_VALUE};

        // When
        writeCommand.execute(args);

        // Then
        verify(mockDocument).write(3, 1, TEST_VALUE );

    }

    @Test
    void Write_Input_value_오류_확인() {

        String[] args = {"write", "3", "ABCDFFFF"};

        // When
        writeCommand.execute(args);

        // Then
        // Then
        String output = outputStream.toString().trim();
        assertTrue(output.contains("INVALID COMMAND"));
        verify(mockDocument, never()).write(3, 1,TEST_VALUE );
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
    void Read_파라미터_오류_확인() {

        String[] args = {"readfile" , "3"};

        // When
        readCommand.execute(args);

        // Then
        String output = outputStream.toString().trim();
        assertTrue(output.contains("INVALID COMMAND"));
        verify(mockDocument, never()).read(3,1);
    }


}
