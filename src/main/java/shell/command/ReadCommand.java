package shell.command;

import shell.util.Utility;

public class ReadCommand implements Command {
    private Document document;
    Utility util;

    int lba;
    int size;
    CommandType readType;

    public ReadCommand (Document document) {
        this.document = document;
        this.util = Utility.getLogger();

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
            System.out.println("INVALID COMMAND");
            return;
        }
        setArgument(args);

        document.read(lba, size);
    }
}
