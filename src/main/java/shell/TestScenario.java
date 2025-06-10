package shell;

import java.util.Random;

public class TestScenario {
    TestShell testShell;

    public TestScenario(TestShell testShell) {
        this.testShell = testShell;
    }


    private String readCompare(int i, String s) {
        String result = testShell.read(i);
        if(!result.equals(s)) return "FAIL";
        return "PASS";
    }

    public String fullWriteAndReadCompare(long seed) {
        Random rand = new Random(seed);
        for(int i=0;i<20;i++){
            for(int j=0;j<5;j++){
                String hexString = getRandomHexString(rand);
                testShell.write(i*5+j,hexString);
                String result = readCompare(i*5+j,hexString);
                if(result.equals("FAIL")) return result;
            }
        }
        return "PASS";
    }

    public String partialLBAWrite() {
        for(int i=0;i<30;i++){
            testShell.write(4,"0xFFFFFFFF");
            testShell.write(0,"0xFFFFFFFF");
            testShell.write(3,"0xFFFFFFFF");
            testShell.write(1,"0xFFFFFFFF");
            testShell.write(2,"0xFFFFFFFF");
            for(int j=0;j<5;j++){
                String result = readCompare(j,"0xFFFFFFFF");
                if(result.equals("FAIL")) return result;
            }
        }
        return "PASS";
    }

    public String writeReadAging(long seed) {
        Random rand = new Random(seed);

        for(int i=0;i<200;i++){
            String hexString = getRandomHexString(rand);
            testShell.write(0,hexString);
            String result = readCompare(0,hexString);
            if(result.equals("FAIL")) return result;
            hexString = getRandomHexString(rand);
            testShell.write(99,hexString);
            result = readCompare(0,hexString);
            if(result.equals("FAIL")) return result;
        }
        return "PASS";
    }

    private String getRandomHexString(Random rand) {
        // 32비트 난수 생성 (0 ~ 0xFFFFFFFF)
        int randomValue = rand.nextInt(); // 기본적으로 -2^31 ~ 2^31-1 범위의 값
        // 16진수로 출력
        return "0x"+String.format("%08X", randomValue);
    }
}
