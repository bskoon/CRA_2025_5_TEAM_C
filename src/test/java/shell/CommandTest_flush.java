package shell;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import shell.command.CommandLibrary;
import shell.command.FlushCommand;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CommandTest_flush {
    private FlushCommand flushCommand;
    private CommandLibrary mockCommandLibrary;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setup() {
        // Arrange
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        mockCommandLibrary = mock(CommandLibrary.class);
        flushCommand = new FlushCommand(mockCommandLibrary);
    }

    @AfterEach
    void tearDown() {
        // System.out 복원
        System.setOut(originalOut);
    }

    @Test
    void Flush_정상_실행_테스트(){
        // Given
        String[] args = {"flush"};

        // When
        flushCommand.execute(args);

        // Then
        verify(mockCommandLibrary).flush();
    }
}
