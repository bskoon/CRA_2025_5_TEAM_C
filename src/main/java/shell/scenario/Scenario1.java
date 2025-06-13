package shell.scenario;

import shell.util.SSDCaller;

import java.util.Random;

public class Scenario1 extends TestScenario {
    public Scenario1(SSDCaller ssdCaller) {
        super(ssdCaller);
    }

    public Scenario1(SSDCaller ssdCaller, Random random) {
        super(ssdCaller, random);
    }

    @Override
    public String executeScenario() {
        try {
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 5; j++) {
                    String hexString = getRandomHexString(rand);
                    ssdCaller.writeOnSSD(i * 5 + j, hexString);
                    String result = readCompare(i * 5 + j, hexString);
                    if (result.equals("FAIL")) return result;
                }
            }
        } catch (Exception e) {
            logger.log("Scenario1.executeScenario()", "Exception while execute scenario");
            return "FAIL";
        }

        return "PASS";
    }
}
