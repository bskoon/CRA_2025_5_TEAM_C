package shell.command;

import shell.util.Utility;
import static shell.util.ShellConstant.*;

public class EraseCommand implements Command {
    private Document document;
    Utility util;
    
    int lba;
    int size;
    CommandType eraseType;

    public EraseCommand (Document document) {
        this.document = document;
        this.util = Utility.getInstance();
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
        int intArg1 = Integer.parseInt(args[1]);
        int intArg2 = Integer.parseInt(args[2]);
        if (eraseType == CommandType.erase) {
            if (intArg2 >= 0) {
                lba = intArg1;
                size = Math.min(intArg2, MAX_SSD_BLOCK - lba);
            }
            else {
                lba = Math.max(intArg1 + intArg2 + 1, 0);
                size = Math.min(-intArg2, intArg1 + 1);
            }
        }
        else if (eraseType == CommandType.erase_range) {
            int lower = Math.min(intArg1, intArg2);
            int upper = Math.max(intArg1, intArg2);

            lba = lower;
            size = upper - lower + 1;
            size = Math.min(size, MAX_SSD_BLOCK - lba);
        }

    }

    @Override
    public void execute(String[] args) {
        eraseType = CommandType.fromString(args[0]);
        if (!argumentCheck(args)) {
            System.out.println("INVALID COMMAND");
            return;
        }
        setArgument(args);

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
