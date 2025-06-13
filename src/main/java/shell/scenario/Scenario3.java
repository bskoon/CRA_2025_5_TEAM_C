package shell.scenario;

import shell.util.SSDCaller;

import java.io.IOException;
import java.util.Random;

public class Scenario3 extends TestScenario {
    public Scenario3(SSDCaller ssdCaller) {
        super(ssdCaller);
    }

    public Scenario3(SSDCaller ssdCaller, Random random) {
        super(ssdCaller, random);
    }

    @Override
    public String executeScenario() {
        try {
            for (int i = 0; i < 200; i++) {
                if (!writeReadAgingOnce()) return "FAIL";
            }
        } catch (Exception e) {
            logger.log("Scenario1.executeScenario()", "Exception while execute scenario");
            return "FAIL";
        }

        return "PASS";
    }

    private boolean writeReadAgingOnce() throws IOException {
        String hexString = getRandomHexString(rand);
        ssdCaller.writeOnSSD(0, hexString);
        String result = readCompare(0,hexString);
        if(result.equals("FAIL")) return false;
        hexString = getRandomHexString(rand);
        ssdCaller.writeOnSSD(99,hexString);
        result = readCompare(99,hexString);
        if(result.equals("FAIL")) return false;

        return true;
    }
}
