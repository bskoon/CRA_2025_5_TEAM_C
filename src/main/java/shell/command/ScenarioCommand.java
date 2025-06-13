package shell.command;

import shell.util.Utility;

public class ScenarioCommand implements Command {
    private Document document;
    private Utility util;

    CommandType scenarioName;

    public ScenarioCommand (Document document) {
        this.document = document;
        this.util = Utility.getInstance();
    }

    @Override
    public boolean argumentCheck(String[] args) {
        return false;
    }

    @Override
    public void setArgument(String[] args) {
        // Do Nothing
    }


    @Override
    public void execute(String[] args) {
        CommandType scriptType = CommandType.fromString(args[0]);
        document.scenario(scriptType);
    }
}
