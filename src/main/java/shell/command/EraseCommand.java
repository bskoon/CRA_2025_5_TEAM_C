package shell.command;

import shell.util.Logger;
import shell.util.Utility;
import static shell.util.ShellConstant.*;

public class EraseCommand implements Command {
    private static final Logger log = Logger.getLogger();

    private Document document;
    Utility util = Utility.getInstance();

    int lba;
    int size;
    CommandType eraseType;

    public EraseCommand(Document document) {
        this.document = document;
    }

    @Override
    public boolean isVaildArgument(String[] args) {
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
            setParameterFromErase(intArg2, intArg1);
        }
        else if (eraseType == CommandType.erase_range) {
            setParameterFromEraseRange(intArg1, intArg2);
        }
    }

    private void setParameterFromErase(int intArg2, int intArg1) {
        if (intArg2 >= 0) {
            lba = intArg1;
            size = Math.min(intArg2, MAX_SSD_BLOCK - lba);
        }
        else {
            lba = Math.max(intArg1 + intArg2 + 1, 0);
            size = Math.min(-intArg2, intArg1 + 1);
        }
    }

    private void setParameterFromEraseRange(int intArg1, int intArg2) {
        int lower = Math.min(intArg1, intArg2);
        int upper = Math.max(intArg1, intArg2);

        lba = lower;
        size = upper - lower + 1;
        size = Math.min(size, MAX_SSD_BLOCK - lba);
    }

    @Override
    public void execute(String[] args) {
        eraseType = CommandType.fromString(args[0]);
        if (!isVaildArgument(args)) {
            log.print("INVALID COMMAND");
            return;
        }
        setArgument(args);

        log.log("EraseCommand.execute()", "Execute ERASE - LBA:" + lba + "  SIZE:" + size);
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
