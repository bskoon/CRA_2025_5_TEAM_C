package shell;

import java.io.IOException;
import java.util.Random;

public class TestScenario {
    TestShell testShell;
    Random rand;

    public TestScenario(TestShell testShell, Random rand) {
        this.testShell = testShell;
        this.rand = rand;
    }


    private String readCompare(int i, String s) throws IOException {
        String result = testShell.readLBA(i);
            if(!result.equals(s)) return "FAIL";
        return "PASS";
    }

    public String fullWriteAndReadCompare() throws IOException {
        for(int i=0;i<20;i++){
            for(int j=0;j<5;j++){
                String hexString = getRandomHexString(rand);
                testShell.writeLBA(i*5+j,hexString);
                String result = readCompare(i*5+j,hexString);
                if(result.equals("FAIL")) return result;
            }
        }
        return "PASS";
    }

    public String partialLBAWrite() throws IOException {
        for(int i=0;i<30;i++){
            testShell.writeLBA(4,"0xFFFFFFFF");
            testShell.writeLBA(0,"0xFFFFFFFF");
            testShell.writeLBA(3,"0xFFFFFFFF");
            testShell.writeLBA(1,"0xFFFFFFFF");
            testShell.writeLBA(2,"0xFFFFFFFF");
            for(int j=0;j<5;j++){
                String result = readCompare(j,"0xFFFFFFFF");
                if(result.equals("FAIL")) return result;
            }
        }
        return "PASS";
    }

    private String getRandomHexString(Random rand) {
        // 32비트 난수 생성 (0 ~ 0xFFFFFFFF)
        int randomValue = rand.nextInt(); // 기본적으로 -2^31 ~ 2^31-1 범위의 값
        // 16진수로 출력
        return "0x"+String.format("%08X", randomValue);
    }

    public boolean writeReadAgingOnce() throws IOException {
        String hexString = getRandomHexString(rand);
        testShell.writeLBA(0,hexString);
        String result = readCompare(0,hexString);
        if(result.equals("FAIL")) return false;
        hexString = getRandomHexString(rand);
        testShell.writeLBA(99,hexString);
        result = readCompare(99,hexString);
        if(result.equals("FAIL")) return false;

        return true;
    }
}
