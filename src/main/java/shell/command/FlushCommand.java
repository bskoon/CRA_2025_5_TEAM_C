package shell.command;

import shell.util.Logger;

public class FlushCommand implements Command {
    private static final Logger log = Logger.getLogger();

    Document document;

    public FlushCommand(Document document) {
        this.document = document;
    }

    @Override
    public boolean argumentCheck(String[] args) {
        return true;
    }

    @Override
    public void setArgument(String[] args) {
        // Do Nothing
    }

    @Override
    public void execute(String[] args) {
        if (!argumentCheck(args)) { }
        setArgument(args);

        log.log("FlushCommand.execute()", "Execute FLUSH");
        document.flush();
    }
}
