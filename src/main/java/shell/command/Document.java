package shell.command;

import shell.util.Logger;
import shell.util.SSDCaller;
import shell.util.TestScenario;

public class Document {
    private static final Logger log = Logger.getLogger();

    private SSDCaller ssdCaller;
    private TestScenario testScenario;

    public Document() {
        ssdCaller = SSDCaller.getInstance();
        testScenario = new TestScenario(ssdCaller);
    }

    public void read(int lba, int size) {
        for (int idx = 0; idx < size; idx++) {
            String readVal = ssdCaller.readOnSSD(lba + idx);
            log.log("Document.read()", "Return READ - LBA:" + lba + "  DATA:" + readVal);
            log.print("LBA " + String.format("%02d", lba + idx) + ": " + readVal);
        }
    }

    public void write(int lba, int size, String updateData) {
        for (int idx = 0; idx < size; idx++)
            ssdCaller.writeOnSSD(lba + idx, updateData);
    }

    public void erase(int lba, int size) {
        ssdCaller.eraseOnSSD(lba, size);
    }

    public void flush() {
        ssdCaller.flushSSD();
    }

    public void scenario(CommandType type) {
        String scenarioResult = "";
        try {
            switch (type) {
                case script1:
                    scenarioResult = testScenario.fullWriteAndReadCompare();
                    break;
                case script2:
                    scenarioResult = testScenario.partialLBAWrite();
                    break;
                case script3:
                    scenarioResult = testScenario.writeReadAging();
                    break;
                case script4:
                    scenarioResult = testScenario.eraseAndWriteAging();
                    break;
                default:
                    scenarioResult = "INVALID ARGUMENT";
                    break;
            }
        } catch (Exception ignored) {
        }

        log.log("Document.scenario()", "Result SCENARIO :" + scenarioResult);
        log.print(scenarioResult);
    }
}
