package shell.scenario;

import shell.util.SSDCaller;

import static shell.util.ShellConstant.WRITECOMMAND;

public class Scenario2 extends TestScenario {
    public Scenario2(SSDCaller ssdCaller) {
        super(ssdCaller);
    }

    @Override
    public String executeScenario() {
        String compareValue = "0xFFFFFFFF";

        try {
            for (int i = 0; i < 30; i++) {
                ssdCaller.callSSD(WRITECOMMAND, "4", compareValue);
                ssdCaller.callSSD(WRITECOMMAND, "0", compareValue);
                ssdCaller.callSSD(WRITECOMMAND, "3", compareValue);
                ssdCaller.callSSD(WRITECOMMAND, "1", compareValue);
                ssdCaller.callSSD(WRITECOMMAND, "2", compareValue);

                for (int idx = 0; idx < 5; idx++) {
                    String result = readCompare(Integer.toString(idx), "0xFFFFFFFF");
                    if (result.equals(FAIL)) return result;
                }
            }
        } catch (Exception e) {
            logger.log("Scenario2.executeScenario()", "Exception while execute scenario");
            return FAIL;
        }
        return PASS;
    }
}
