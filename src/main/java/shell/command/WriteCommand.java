package shell.command;

import shell.Command;
import shell.Document;

public class WriteCommand implements Command {
    private static final int WRITE_ARG_COUNT = 3;
    private static final int FULLWRITE_ARG_COUNT = 2;
    private static final int MAX_SSD_BLOCK = 100;

    private Document document;

    public WriteCommand (Document document) {
        this.document = document;
    }

    public boolean isArgumentValid(int argLength, boolean isFull, int lba, String updateData) {
        if(!isValidParameter(argLength, isFull)) return false;
        if(!isValidLBA(lba)) return false;
        if(!isValidUpdateData(updateData)) return false;
        return true;
    }

    public boolean isValidParameter(int argLength, boolean isFull) {
        if (isFull) return argLength == FULLWRITE_ARG_COUNT;
        return argLength == WRITE_ARG_COUNT;
    }

    public boolean isValidLBA(int lba) {
        return lba >= 0 && lba < MAX_SSD_BLOCK;
    }

    public boolean isValidUpdateData(String updateData) {
        return updateData.matches("0x[0-9A-F]{8}");
    }

    @Override
    public void execute(String[] args) {
        boolean isFull = args[0].equals("fullwrite");

        int lba = 0;
        int size = MAX_SSD_BLOCK;
        String updateData = args[1];

        if (!isFull) {
            lba = Integer.parseInt(args[1]);
            size = MAX_SSD_BLOCK;
            updateData = args[2];
        }

        if (!isArgumentValid(args.length, isFull, lba, updateData)) {
            System.out.println("INVALID COMMAND");
            return;
        }

        document.write(lba, size, updateData);
    }
}
