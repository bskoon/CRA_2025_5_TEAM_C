package shell.scenario;

import shell.util.SSDCaller;

import java.util.Random;

import static shell.util.ShellConstant.MAX_SSD_BLOCK;

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
            ssdCaller.callSSD("E", "0", "3");
            for (int lba = 0; lba < 3; lba++) {
                String result = readCompare(Integer.toString(lba), "0x00000000");
                if (result.equals(FAIL)) return FAIL;
            }

            for (int i = 0; i < 30; i++) {
                for (int lba = 2; lba <= 98; lba += 2) {
                    String firstData = "0xABCD1234";
                    String overwriteData = "0x1234ABCD";
                    int eraseSize = Math.min(3, MAX_SSD_BLOCK - lba);

                    ssdCaller.callSSD("W", Integer.toString(lba), firstData);
                    ssdCaller.callSSD("W", Integer.toString(lba), overwriteData);
                    ssdCaller.callSSD("E", Integer.toString(lba), Integer.toString(eraseSize));

                    for (int idx = 0; idx < eraseSize; idx++) {
                        String result = readCompare(Integer.toString(lba + idx), "0x00000000");
                        if (result.equals(FAIL)) return FAIL;
                    }
                }
            }
        } catch (Exception e) {
            logger.log("Scenario1.executeScenario()", "Exception while execute scenario");
            return FAIL;
        }
        return PASS;
    }
}
