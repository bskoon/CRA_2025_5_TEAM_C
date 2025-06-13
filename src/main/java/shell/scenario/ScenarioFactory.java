package shell.scenario;

import shell.command.CommandType;
import shell.util.SSDCaller;

public class ScenarioFactory {
    SSDCaller ssdCaller;
    public ScenarioFactory(SSDCaller ssdCaller) {
        this.ssdCaller = ssdCaller;
    }

    public TestScenario getScenario(CommandType type) {
        switch (type) {
            case scenario1:
                return new Scenario1(ssdCaller);
            case scenario2:
                return new Scenario2(ssdCaller);
            case scenario3:
                return new Scenario3(ssdCaller);
            default:
                return null;
        }
    }
}
