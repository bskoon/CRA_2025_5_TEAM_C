package shell.command;

import shell.util.Logger;

public class ScenarioCommand implements Command {
    private static final Logger log = Logger.getLogger();

    private Document document;

    public ScenarioCommand(Document document) {
        this.document = document;
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
        log.log("ScenarioCommand.execute()", "Execute SCENARIO - NAME:" + args[0]);
        document.scenario(scriptType);
    }
}
