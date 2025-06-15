package shell.command;

import shell.util.Logger;

public class FlushCommand implements Command {
    private static final Logger log = Logger.getLogger();

    CommandLibrary commandLibrary;

    public FlushCommand(CommandLibrary commandLibrary) {
        this.commandLibrary = commandLibrary;
    }

    @Override
    public boolean isVaildArgument(String[] args) {
        return true;
    }

    @Override
    public void setArgument(String[] args) {
        // Do Nothing
    }

    @Override
    public void execute(String[] args) {
        if (!isVaildArgument(args)) { }
        setArgument(args);

        log.log("FlushCommand.execute()", "Execute FLUSH");
        commandLibrary.flush();
    }
}
