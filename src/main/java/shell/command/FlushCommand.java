package shell.command;

public class FlushCommand implements Command {
    private static final int FLUSH_ARG_COUNT = 1;
    Document document;

    public FlushCommand (Document document) {
        this.document = document;
    }

    public boolean isArgumentValid(int argLength) {
        if (!isValidParameter(argLength)) return false;
        return true;
    }

    private boolean isValidParameter(int argLength) {
        return argLength == FLUSH_ARG_COUNT;
    }

    @Override
    public void execute(String[] args) {
        if (!isArgumentValid(args.length)) {
            System.out.println("INVALID COMMAND");
            return;
        }

        document.flush();
    }
}
