package shell.command;

import shell.Command;
import shell.Document;

public class EraseCommand implements Command {
    private Document document;

    public EraseCommand (Document document) {
        this.document = document;
    }

    @Override
    public void execute(String[] args) {
        int lba = Integer.parseInt(args[1]);
        int size = Integer.parseInt(args[2]);
        document.erase(lba, size);
    }
}
