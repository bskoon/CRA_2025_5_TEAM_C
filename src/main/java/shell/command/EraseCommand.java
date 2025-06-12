package shell.command;

import shell.Command;
import shell.Document;

public class EraseCommand implements Command {
    private Document document;

    public EraseCommand (Document document) {
        this.document = document;
    }

    public boolean isArgumentValid(int argLength, boolean isFull, int lba) {
        return true;
    }

    public boolean isValidParameter(int argLength, boolean isFull) {
        return false;
    }

    public boolean isValidLBA(int lba) {
        return false;
    }

    @Override
    public void execute(String[] args) {
        int startLBA = 0;
        int endLBA = 0;

        document.erase(startLBA, endLBA);
    }
}
