package shell.command;

import shell.scenario.ScenarioFactory;
import shell.scenario.TestScenario;
import shell.util.Logger;
import shell.util.SSDCaller;

public class Document {
    private static final Logger log = Logger.getLogger();

    private SSDCaller ssdCaller = SSDCaller.getInstance();

    public void read(int lba, int size) {
        for (int idx = 0; idx < size; idx++) {
            String readVal = ssdCaller.readOnSSD(lba + idx);
            log.log("Document.read()", "Return READ - LBA:" + (lba + idx) + "  DATA:" + readVal);
            log.print("LBA " + String.format("%02d", lba + idx) + ": " + readVal);
        }
    }

    public void write(int lba, int size, String updateData) {
        for (int idx = 0; idx < size; idx++) {
            ssdCaller.writeOnSSD(lba + idx, updateData);
            log.log("Document.write()", "WRITE FINISH - LBA:" + (lba + idx) + "  DATA:" + updateData);
        }
    }

    public void erase(int lba, int size) {
        ssdCaller.eraseOnSSD(lba, size);
        log.log("Document.erase()", "ERASE FINISH - START LBA:" + lba + ", SIZE:" + size);
    }

    public void flush() {
        ssdCaller.flushSSD();
    }

    public void scenario(CommandType type) {
        TestScenario testScenario = new ScenarioFactory(ssdCaller).getScenario(type);
        if (testScenario == null) {
            log.log("Document.scenario()", "Not implemented Scenario");
            log.print("INVALID ARGUMENT");
        }

        String scenarioResult = testScenario.executeScenario();
        log.log("Document.scenario()", "Result SCENARIO :" + scenarioResult);
        System.out.println(scenarioResult);
    }
}
