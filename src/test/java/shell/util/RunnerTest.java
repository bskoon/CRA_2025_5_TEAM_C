package shell.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import shell.command.CommandExecutor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RunnerTest {
    public static final String SCENARIO1 = "scenario1";

    private File tempScriptFile;
    private CommandExecutor mockExecutor;

    @BeforeEach
    public void setup() throws IOException {
        mockExecutor = mock(CommandExecutor.class);

        tempScriptFile = File.createTempFile("test_script", ".txt");
        Files.write(tempScriptFile.toPath(), List.of(SCENARIO1));
    }

    @AfterEach
    public void cleanup() {
        tempScriptFile.delete();
    }

    @Test
    public void testInvalidFileThrowsException() {
        File invalidFile = new File("nonexistent_file.txt");
        Exception e = assertThrows(RuntimeException.class, () -> {
            new Runner(new String[]{invalidFile.getAbsolutePath()}, mockExecutor);
        });

        assertEquals("Invalid File Name", e.getMessage());
    }

    @Test
    public void testPassOutput() {
        doAnswer(invocation -> {
            System.out.print("PASS");
            return null;
        }).when(mockExecutor).executeCommand(new String[]{SCENARIO1});

        String output = getSysoutMessage();
        assertTrue(output.contains(SCENARIO1 + "   ___   Run...Pass"));
    }

    @Test
    public void testFailOutput() {
        doAnswer(invocation -> {
            System.out.print("FAIL");
            return null;
        }).when(mockExecutor).executeCommand(new String[]{SCENARIO1});

        String output = getSysoutMessage();
        assertTrue(output.contains(SCENARIO1 + "   ___   Run...FAIL!"));
    }

    private String getSysoutMessage() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        new Runner(new String[]{tempScriptFile.getAbsolutePath()}, mockExecutor).run();

        System.setOut(System.out);

        return outContent.toString().replace("\r\n", "");
    }
}