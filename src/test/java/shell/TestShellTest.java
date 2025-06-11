import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import shell.TestShell;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestShellTest {

    private TestShell shell;

    @BeforeEach
    void setup() {
        shell = spy(new TestShell());
    }

    @Test
    void testWriteAndReadSameLBA_withMock() {
        // given
        doReturn(true).when(shell).isValidLBA("5");
        doReturn(true).when(shell).isValidValue("0xA1B2C3D4");
        doNothing().when(shell).writeLBA(5, "0xA1B2C3D4");
        doReturn("A1B2C3D4").when(shell).readLBA(5);

        String input = String.join("\n",
                "write 5 0xA1B2C3D4",
                "read 5",
                "exit"
        );
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        //shell.scanner = new Scanner(inputStream);
        shell.setScanner(new Scanner(inputStream));

        // when
        shell.launchShell();

        // then
        verify(shell).writeLBA(5, "0xA1B2C3D4");
        verify(shell).readLBA(5);
    }

    @Test
    void testInvalidCommandHandling() {
        String input = String.join("\n",
                "foobar",
                "exit"
        );
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        shell.scanner = new Scanner(inputStream);

        shell.launchShell();
    }

    @Test
    void testFullWriteAndFullRead_withMock() {
        doReturn(true).when(shell).isValidValue("0xFFFFFFFF");
        doReturn("FFFFFFFF").when(shell).readLBA(anyInt());

        doNothing().when(shell).writeLBA(anyInt(), eq("0xFFFFFFFF"));

        String input = String.join("\n",
                "fullwrite 0xFFFFFFFF",
                "fullread",
                "exit"
        );
        shell.scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        shell.launchShell();

        verify(shell, times(100)).writeLBA(anyInt(), eq("0xFFFFFFFF"));
        verify(shell, times(100)).readLBA(anyInt());
    }

    @AfterEach
    void cleanup() {
        shell = null;
    }
}