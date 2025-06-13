package shell.command;

import shell.util.Logger;
import shell.util.Utility;

public class WriteCommand implements Command {
    private static final Logger log = Logger.getLogger();

    private Document document;
    private Utility util = Utility.getInstance();

    int lba;
    int size;
    String updateData;
    CommandType writeType;

    public WriteCommand(Document document) {
        this.document = document;
        this.lba = 0;
        this.size = util.MAX_SSD_BLOCK;
    }

    @Override
    public boolean argumentCheck(String[] args) {
        if (writeType == CommandType.write) {
            if (!util.isValidLBA(args[1])) {
                return false;
            }
            if (!util.isValidUpdateData(args[2])) {
                return false;
            }
        } else if (writeType == CommandType.fullwrite) {
            if (!util.isValidUpdateData(args[1])) {
                return false;
            }
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
            updateData = args[1];
        }
    }

    @Override
    public void execute(String[] args) {
        writeType = CommandType.fromString(args[0]);
        if (!argumentCheck(args)) {
            log.print("INVALID COMMAND");
            return;
        }
        setArgument(args);

        log.log("WriteCommand.execute()", "Execute WRITE - LBA:" + lba + "  SIZE:" + size + "  DATA:" + updateData);
        document.write(lba, size, updateData);
    }
}
