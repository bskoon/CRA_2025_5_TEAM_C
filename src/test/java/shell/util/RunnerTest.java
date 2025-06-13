package shell.util;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import shell.command.CommandExecutor;

import static org.junit.jupiter.api.Assertions.*;

class RunnerTest {

    @Mock
    CommandExecutor executor;

    @Test
    void Runner_scriptName_파일이_없는_경우() {
        String scriptName = "shell_script.txt";

        Runner runner = new Runner(new String[]{scriptName}, executor);

        assertThrows(RuntimeException.class, () -> {

        });

    }


}