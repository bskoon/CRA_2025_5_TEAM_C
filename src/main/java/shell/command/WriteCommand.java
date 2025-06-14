package shell.command;

import shell.util.Logger;
import shell.util.Utility;
import static shell.util.ShellConstant.*;

public class WriteCommand implements Command {
    private static final Logger log = Logger.getLogger();

    private CommandLibrary commandLibrary;
    private Utility util = Utility.getInstance();

    int lba;
    int size;
    String updateData;
    CommandType writeType;

    public WriteCommand(CommandLibrary commandLibrary) {
        this.commandLibrary = commandLibrary;
        this.lba = 0;
        this.size = MAX_SSD_BLOCK;
    }

    @Override
    public boolean isVaildArgument(String[] args) {
        if (writeType == CommandType.write) {
            if (!isWriteArgumentValid(args)) return false;
        } else if (writeType == CommandType.fullwrite) {
            if (!isFullWriteArgumentValid(args)) return false;
        }
        return true;
    }

    private boolean isWriteArgumentValid(String[] args) {
        if (!util.isValidLBA(args[1])) {
            return false;
        }
        if (!util.isValidUpdateData(args[2])) {
            return false;
        }
        return true;
    }

    private boolean isFullWriteArgumentValid(String[] args) {
        if (!util.isValidUpdateData(args[1])) {
            return false;
        }
        return true;
    }

    @Override
    public void setArgument(String[] args) {
        if (writeType == CommandType.write) {
            lba = Integer.parseInt(args[1]);
            size = 1;
            updateData = args[2];
        } else if (writeType == CommandType.fullwrite) {
            lba = 0;
            size = MAX_SSD_BLOCK;
            updateData = args[1];
        }
    }

    @Override
    public void execute(String[] args) {
        writeType = CommandType.fromString(args[0]);
        if (!isVaildArgument(args)) {
            log.print("INVALID COMMAND");
            return;
        }

        setArgument(args);

        log.log("WriteCommand.execute()", "Execute WRITE - LBA:" + lba + "  SIZE:" + size + "  DATA:" + updateData);
        commandLibrary.write(lba, size, updateData);
    }
}
