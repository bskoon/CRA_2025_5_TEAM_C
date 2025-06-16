package shell.scenario;

import shell.command.CommandType;
import shell.util.Logger;
import shell.util.SSDCaller;

public class ScenarioFactory {
    SSDCaller ssdCaller;
    private Logger log = Logger.getLogger();

    public ScenarioFactory(SSDCaller ssdCaller) {
        this.ssdCaller = ssdCaller;
    }

    public TestScenario getScenario(CommandType type) {
        if (type == null) {
            log.log("ScenarioFactory.getScenario()", "Not Implemented Scenario");
            return null;
        }
        return type.getScenario(ssdCaller);
    }
}
