package shell.scenario;

import shell.util.SSDCaller;
import shell.util.Logger;

import java.util.Random;

import static shell.util.ShellConstant.READCOMMAND;

public abstract class TestScenario {
    protected static final String PASS = "PASS";
    protected static final String FAIL = "FAIL";

    protected SSDCaller ssdCaller;
    protected Random rand;
    protected Logger logger;

    public TestScenario(SSDCaller ssdCaller) {
        this.ssdCaller = ssdCaller;
        this.rand = new Random();
        this.logger = Logger.getLogger();
    }

    public TestScenario(SSDCaller ssdCaller, Random random) {
        this.ssdCaller = ssdCaller;
        this.rand = random;
        this.logger = Logger.getLogger();
    }

    public String readCompare(String lba, String expectedData) {
        ssdCaller.callSSD(READCOMMAND, lba);
        String result = ssdCaller.readSSDData();

        if (result.equals(expectedData)) return PASS;
        return FAIL;
    }

    protected String getRandomHexString(Random rand) {
        int randomValue = rand.nextInt();
        return "0x"+String.format("%08X", randomValue);
    }

    public abstract String executeScenario();
}
