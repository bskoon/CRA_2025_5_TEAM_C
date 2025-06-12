package shell.command;

import shell.Command;
import shell.Document;

public class ReadCommand implements Command {
    private Document document;

    public ReadCommand (Document document) {
        this.document = document;
    }

    @Override
    public void execute(String[] args) {
        int lba = Integer.parseInt(args[1]);
        document.read(lba);
    }
}
