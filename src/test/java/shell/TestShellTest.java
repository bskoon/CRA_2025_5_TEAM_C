package shell;

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
    void testInvalidCommandHandling() {
        String input = String.join("\n",
                "foobar",
                "exit"
        );
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        shell.scanner = new Scanner(inputStream);

        shell.launchShell();
    }

    @AfterEach
    void cleanup() {
        shell = null;
    }
}
