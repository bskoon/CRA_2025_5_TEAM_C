package shell.command;

public class FlushCommand implements Command {
    Document document;

    public FlushCommand (Document document) {
        this.document = document;
    }

    @Override
    public void execute(String[] args) {
        document.flush();
    }
}
