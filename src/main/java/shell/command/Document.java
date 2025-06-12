package shell.command;

import shell.util.TestScenario;
import shell.util.SSDCaller;

public class Document {
    private SSDCaller ssdCaller;
    private TestScenario testScenario;

    public Document() {
        ssdCaller = SSDCaller.getSSDCaller();
        testScenario = new TestScenario(ssdCaller);
    }

    public void read(int lba, int size) {
        for (int idx = 0; idx < size; idx++) {
            String readVal = ssdCaller.readOnSSD(lba + idx);
            System.out.println("LBA " + String.format("%02d", lba + idx) + ": " + readVal);
        }
    }

    public void write(int lba, int size, String updateData) {
        for (int idx = 0; idx < size; idx++) {
            ssdCaller.writeOnSSD(lba + idx, updateData);
        }
    }

    public void erase(int lba, int size) {
        ssdCaller.eraseOnSSD(lba, size);
    }

    public void scenario(int scenarioNum) {
        String scenarioResult = "";
        try {
            switch (scenarioNum) {
                case 1:
                    scenarioResult = testScenario.fullWriteAndReadCompare();
                    break;
                case 2:
                    scenarioResult = testScenario.partialLBAWrite();
                    break;
                case 3:
                    scenarioResult = testScenario.writeReadAging();
                    break;
                case 4:
                    scenarioResult = testScenario.eraseAndWriteAging();
                    break;
                default:
                    scenarioResult = "INVLIAD ARGUMENT";
                    break;
            }
        } catch (Exception e) {

        }
        System.out.println(scenarioResult);
    }
}
