package shell.scenario;

import shell.util.SSDCaller;

import java.util.Random;

public class Scenario4 extends TestScenario {
    public Scenario4(SSDCaller ssdCaller) {
        super(ssdCaller);
    }

    public Scenario4(SSDCaller ssdCaller, Random random) {
        super(ssdCaller, random);
    }

    @Override
    public String executeScenario() {
        try {
            ssdCaller.eraseOnSSD(0, 3);
            for (int i = 0; i < 3; i++) {
                String result = readCompare(i, "0x00000000");
                if (result.equals("FAIL")) return result;
            }
            for (int i = 0; i < 30; i++) {
                for (int lba = 2; lba <= 98; lba += 2) {
                    ssdCaller.writeOnSSD(lba, "0xABCD1234");
                    ssdCaller.writeOnSSD(lba, "0x1234ABCD");
                    ssdCaller.eraseOnSSD(lba, 3);
                    for (int j = 0; j < 3; j++) {
                        String result = readCompare(j, "0x00000000");
                        if (result.equals("FAIL")) return result;
                    }
                }
            }
        } catch (Exception e) {
            logger.log("Scenario1.executeScenario()", "Exception while execute scenario");
            return "FAIL";
        }
        return "PASS";
    }
}
