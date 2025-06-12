package shell;

public class ReadCommand implements Command {
    private Document document;

    public ReadCommand (Document document) {
        this.document = document;
    }

    @Override
    public void execute() {
        document.read();
    }
}
