package shell.util;

import shell.TestShell;

import java.io.IOException;
import java.util.Random;

public class TestScenario {
    private SSDCaller ssdCaller;
    private Random rand;

    public TestScenario(SSDCaller ssdCaller) {
        this.ssdCaller = ssdCaller;
        this.rand = new Random();
    }

    public TestScenario(SSDCaller ssdCaller, Random random) {
        this.ssdCaller = ssdCaller;
        this.rand = random;
    }

    public String fullWriteAndReadCompare() throws IOException {
        for(int i=0;i<20;i++){
            for(int j=0;j<5;j++){
                String hexString = getRandomHexString(rand);
                ssdCaller.writeOnSSD(i*5+j,hexString);
                String result = readCompare(i*5+j,hexString);
                if(result.equals("FAIL")) return result;
            }
        }
        return "PASS";
    }

    public String partialLBAWrite() throws IOException {
        for(int i=0;i<30;i++){
            ssdCaller.writeOnSSD(4,"0xFFFFFFFF");
            ssdCaller.writeOnSSD(0,"0xFFFFFFFF");
            ssdCaller.writeOnSSD(3,"0xFFFFFFFF");
            ssdCaller.writeOnSSD(1,"0xFFFFFFFF");
            ssdCaller.writeOnSSD(2,"0xFFFFFFFF");
            for(int j=0;j<5;j++){
                String result = readCompare(j,"0xFFFFFFFF");
                if(result.equals("FAIL")) return result;
            }
        }
        return "PASS";
    }

    public String writeReadAging() throws IOException {
        for(int i=0;i<200;i++){
            if(!writeReadAgingOnce()) return "FAIL";
        }
        return "PASS";
    }

    public String eraseAndWriteAging() {
        return "";
    }

    public String readCompare(int i, String s) throws IOException {
        String result = ssdCaller.readOnSSD(i);

        if(!result.equals(s)) return "FAIL";
        return "PASS";
    }

    private String getRandomHexString(Random rand) {
        // 32비트 난수 생성 (0 ~ 0xFFFFFFFF)
        int randomValue = rand.nextInt(); // 기본적으로 -2^31 ~ 2^31-1 범위의 값
        // 16진수로 출력
        return "0x"+String.format("%08X", randomValue);
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
