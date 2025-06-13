package shell.scenario;

import shell.util.SSDCaller;
import shell.util.Logger;

import java.io.IOException;
import java.util.Random;

public abstract class TestScenario {
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

    public String readCompare(int i, String s) throws IOException {
        String result = ssdCaller.readOnSSD(i);

        if(!result.equals(s)) return "FAIL";
        return "PASS";
    }

    protected String getRandomHexString(Random rand) {
        // 32비트 난수 생성 (0 ~ 0xFFFFFFFF)
        int randomValue = rand.nextInt(); // 기본적으로 -2^31 ~ 2^31-1 범위의 값
        // 16진수로 출력
        return "0x"+String.format("%08X", randomValue);
    }

    public abstract String executeScenario();

    public String eraseAndWriteAging() {
        return "";
    }

}
