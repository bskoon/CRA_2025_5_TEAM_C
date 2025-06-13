package shell.command;

import shell.util.Utility;

public class FlushCommand implements Command {
    Document document;
    Utility util;

    public FlushCommand (Document document) {
        this.document = document;
        this.util = Utility.getLogger();
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
        document.flush();
    }
}
