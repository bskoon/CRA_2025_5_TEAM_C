package shell.scenario;

import shell.util.SSDCaller;

import java.io.IOException;
import java.util.Random;

import static shell.util.ShellConstant.WRITECOMMAND;

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
                String hexString = getRandomHexString(rand);
                ssdCaller.callSSD(WRITECOMMAND, "0", hexString);
                String result = readCompare("0", hexString);
                if (result.equals(FAIL)) return FAIL;

                hexString = getRandomHexString(rand);
                ssdCaller.callSSD(WRITECOMMAND, "99", hexString);
                result = readCompare("99", hexString);
                if(result.equals(FAIL)) return FAIL;
            }
        } catch (Exception e) {
            logger.log("Scenario3.executeScenario()", "Exception while execute scenario");
            return FAIL;
        }

        return PASS;
    }
}
