package shell.command;

import shell.scenario.ScenarioFactory;
import shell.scenario.TestScenario;
import shell.util.Logger;
import shell.util.SSDCaller;

public class Document {
    private SSDCaller ssdCaller;
    private Logger logger;

    public Document() {
        this.ssdCaller = SSDCaller.getInstance();
        this.logger = Logger.getLogger();
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

    public void flush() {
        ssdCaller.flushSSD();
    }

    public void scenario(CommandType type) {
        TestScenario testScenario = new ScenarioFactory(ssdCaller).getScenario(type);
        if (testScenario == null) {
            logger.log("Document.scenario()", "Not implemented Scenario");
            logger.print("INVALID ARGUMENT");
        }

        String scenarioResult = testScenario.executeScenario();
        System.out.println(scenarioResult);
    }
}
