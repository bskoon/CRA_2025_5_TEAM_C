package shell.command;

import shell.scenario.ScenarioFactory;
import shell.scenario.TestScenario;
import shell.util.Logger;
import shell.util.SSDCaller;

import static shell.util.ShellConstant.READCOMMAND;
import static shell.util.ShellConstant.WRITECOMMAND;
import static shell.util.ShellConstant.ERASECOMMAND;
import static shell.util.ShellConstant.FLUSHCOMMAND;

public class Document {
    private static final Logger log = Logger.getLogger();

    private SSDCaller ssdCaller = SSDCaller.getInstance();

    public void read(int lba, int size) {
        for (int idx = 0; idx < size; idx++) {
            ssdCaller.callSSD(READCOMMAND, Integer.toString(lba + idx));
            String readVal = ssdCaller.readSSDData();

            log.log("Document.read()", "Return READ - LBA:" + (lba + idx) + "  DATA:" + readVal);
            log.print("LBA " + String.format("%02d", lba + idx) + ": " + readVal);
        }
    }

    public void write(int lba, int size, String updateData) {
        for (int idx = 0; idx < size; idx++) {
            ssdCaller.callSSD(WRITECOMMAND, Integer.toString(lba + idx), updateData);
            log.log("Document.write()", "WRITE FINISH - LBA:" + (lba + idx) + "  DATA:" + updateData);
        }
    }

    public void erase(int lba, int size) {
        ssdCaller.callSSD(ERASECOMMAND, Integer.toString(lba), Integer.toString(size));
        log.log("Document.erase()", "ERASE FINISH - START LBA:" + lba + ", SIZE:" + size);
    }

    public void flush() {
        ssdCaller.callSSD(FLUSHCOMMAND);
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
