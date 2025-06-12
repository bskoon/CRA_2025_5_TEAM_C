package shell;

import java.io.IOException;
import java.util.Random;

public class TestScenario {
    TestShell testShell;
    Random rand;

    public TestScenario(TestShell testShell, Random rand) {
        this.testShell = testShell;
        this.rand = rand;
    }
}
