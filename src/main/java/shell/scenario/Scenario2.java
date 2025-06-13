package shell.scenario;

import shell.util.SSDCaller;

import java.util.Random;

public class Scenario2 extends TestScenario {
    public Scenario2(SSDCaller ssdCaller) {
        super(ssdCaller);
    }

    public Scenario2(SSDCaller ssdCaller, Random random) {
        super(ssdCaller, random);
    }

    @Override
    public String executeScenario() {
        try {
            for (int i = 0; i < 30; i++) {
                ssdCaller.writeOnSSD(4, "0xFFFFFFFF");
                ssdCaller.writeOnSSD(0, "0xFFFFFFFF");
                ssdCaller.writeOnSSD(3, "0xFFFFFFFF");
                ssdCaller.writeOnSSD(1, "0xFFFFFFFF");
                ssdCaller.writeOnSSD(2, "0xFFFFFFFF");
                for (int j = 0; j < 5; j++) {
                    String result = readCompare(j, "0xFFFFFFFF");
                    if (result.equals("FAIL")) return result;
                }
            }
        } catch (Exception e) {
            logger.log("Scenario2.executeScenario()", "Exception while execute scenario");
            return "FAIL";
        }
        return "PASS";
    }
}
