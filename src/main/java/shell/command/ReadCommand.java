package shell.command;

import shell.util.Logger;
import shell.util.Utility;

public class ReadCommand implements Command {
    private static final Logger log = Logger.getLogger();

    private Document document;
    Utility util = Utility.getInstance();

    int lba;
    int size;
    CommandType readType;

    public ReadCommand (Document document) {
        this.document = document;

        this.lba = 0;
        this.size = util.MAX_SSD_BLOCK;
    }

    @Override
    public boolean argumentCheck(String[] args) {
        if (readType == CommandType.fullread)  return true;
        if (!util.isValidLBA(args[1])) return false;
        return true;
    }

    @Override
    public void setArgument(String[] args) {
        if (readType == CommandType.read) {
            lba = Integer.parseInt(args[1]);
            size = 1;
        }
    }

    @Override
    public void execute(String[] args) {
        readType = CommandType.fromString(args[0]);
        if (!argumentCheck(args)) {
            log.print("INVALID COMMAND");
            return;
        }
        setArgument(args);

        log.log("ReadCommand.execute()", "Execute READ - LBA:" + lba + "  SIZE:" + size);
        document.read(lba, size);
    }
}
