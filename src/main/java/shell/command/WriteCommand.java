package shell.command;

import shell.Command;
import shell.Document;

public class WriteCommand implements Command {
    private Document document;

    public WriteCommand (Document document) {
        this.document = document;
    }

    @Override
    public void execute(String[] args) {
        int lba = Integer.parseInt(args[1]);
        document.write(lba, args[2]);
    }
}
