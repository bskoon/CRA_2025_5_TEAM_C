package shell.command;

import shell.util.Utility;
import static shell.util.ShellConstant.*;

public class WriteCommand implements Command {
    private Document document;
    private Utility util;

    int lba;
    int size;
    String updateData;
    CommandType writeType;

    public WriteCommand (Document document) {
        this.document = document;
        this.util = Utility.getInstance();

        this.lba = 0;
        this.size = MAX_SSD_BLOCK;
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
        } else if(writeType == CommandType.fullwrite) {
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
        } else if(writeType == CommandType.fullwrite) {
            updateData = args[1];
        }
    }

    @Override
    public void execute(String[] args) {
        writeType = CommandType.fromString(args[0]);
        if (!argumentCheck(args)) {
            System.out.println("INVALID COMMAND");
            return;
        }
        setArgument(args);

        document.write(lba, size, updateData);
    }
}
