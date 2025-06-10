package shell;

public class TestScenario {
    TestShell testShell;

    public TestScenario(TestShell testShell) {
        testShell = testShell;
    }


    private String readCompare(int i, String s) {
        String result = testShell.read(i);
        if(!result.equals(s)) return "FAIL";
        return "PASS";
    }

    public String fullWriteAndReadCompare() {
        for(int i=0;i<20;i++){
            for(int j=0;j<5;j++){
                testShell.write(i*5+j,"0xFFFFFFFF");
                String result = readCompare(i*5+j,"0xFFFFFFFF");
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

    public String writeReadAging() {

        for(int i=0;i<200;i++){
            testShell.write(0,"0xFFFFFFFF");
            String result = readCompare(0,"0xFFFFFFFF");
            if(result.equals("FAIL")) return result;
            testShell.write(99,"0xFFFFFFFF");
            result = readCompare(0,"0xFFFFFFFF");
            if(result.equals("FAIL")) return result;
        }
        return "PASS";
    }
}
