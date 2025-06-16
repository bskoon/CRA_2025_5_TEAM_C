package shell;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import shell.command.CommandLibrary;
import shell.command.ReadCommand;

import java.io.*;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandTest_read {
    private static final int MAX_BLOCK_SIZE = 100;

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private Map<Integer, String> testData; // 테스트 데이터 추가
    private CommandLibrary mockCommandLibrary;
    private ReadCommand readCommand;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        mockCommandLibrary = mock(CommandLibrary.class);
        readCommand = new ReadCommand(mockCommandLibrary);
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
        verify(mockCommandLibrary).read(3, 1 );

    }

    @Test
    void Read_비정상_LBA_테스트_확인() {

        String[] readArgs = {"read", "-3"};

        // When
        readCommand.execute(readArgs);

        // Then
        verify(mockCommandLibrary, never()).read(anyInt(), anyInt() );

    }

    @Test
    void FullRead_정상_테스트_확인() {
        // When
        String[] readArgs = {"fullread"};

        // When
        readCommand.execute(readArgs);

        // Then
        verify(mockCommandLibrary).read(0, MAX_BLOCK_SIZE );

    }
}
