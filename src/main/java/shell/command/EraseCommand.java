package shell.command;

import shell.util.Utility;

public class EraseCommand implements Command {
    private Document document;
    Utility util;
    
    int lba;
    int size;
    CommandType eraseType;

    public EraseCommand (Document document) {
        this.document = document;
        this.util = Utility.getLogger();
    }

    @Override
    public boolean argumentCheck(String[] args) {
        if (!util.isValidLBA(args[1])) return false;
        if (eraseType == CommandType.erase_range) {
            if (!util.isValidLBA(args[2])) return false;
        }
        return true;
    }

    @Override
    public void setArgument(String[] args) {
        lba = Integer.parseInt(args[1]);
        size = Integer.parseInt(args[2]);

        if (eraseType == CommandType.erase) {
            size = size - lba + 1;
        }
        size = Math.min(size, util.MAX_SSD_BLOCK - lba);
    }

    @Override
    public void execute(String[] args) {
        eraseType = CommandType.fromString(args[0]);
        if (!argumentCheck(args)) {
            System.out.println("INVALID COMMAND");
            return;
        }

        performEraseInChunks(lba, size);
    }

    private void performEraseInChunks(int lba, int size) {
        final int MAX_CHUNK = 10;

        while (size > 0) {
            int chunk = Math.min(size, MAX_CHUNK);
            document.erase(lba, chunk);
            lba += chunk;
            size -= chunk;
        }
    }
}
