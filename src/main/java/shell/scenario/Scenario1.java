package shell.scenario;

import shell.util.SSDCaller;
import static shell.util.ShellConstant.WRITECOMMAND;

public class Scenario1 extends TestScenario {
    public Scenario1(SSDCaller ssdCaller) {
        super(ssdCaller);
    }

    @Override
    public String executeScenario() {
        try {
            for (int i = 0; i < 20; i++) {
                for (int j = 0; j < 5; j++) {
                    String lba = Integer.toString(i*5 + j);
                    String hexString = getRandomHexString(rand);

                    ssdCaller.callSSD(WRITECOMMAND, lba, hexString);
                    String result = readCompare(lba, hexString);

                    if (result.equals(FAIL)) return result;
                }
            }
        } catch (Exception e) {
            logger.log("Scenario1.executeScenario()", "Exception while execute scenario");
            return FAIL;
        }

        return PASS;
    }
}
