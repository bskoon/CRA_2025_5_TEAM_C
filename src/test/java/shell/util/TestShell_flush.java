package shell.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import shell.command.Document;
import shell.command.FlushCommand;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class TestShell_flush {
    private FlushCommand flushCommand;
    private Document mockDocument;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setup() {
        // Arrange
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        mockDocument = mock(Document.class);
        flushCommand = new FlushCommand(mockDocument);
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
        verify(mockDocument).flush();
    }
}
