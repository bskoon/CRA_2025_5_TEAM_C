package shell.command;

import shell.util.Logger;

public class ScenarioCommand implements Command {
    private static final Logger log = Logger.getLogger();

    private CommandLibrary commandLibrary;

    public ScenarioCommand(CommandLibrary commandLibrary) {
        this.commandLibrary = commandLibrary;
    }

    @Override
    public boolean isVaildArgument(String[] args) {
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
        commandLibrary.scenario(scriptType);
    }
}
