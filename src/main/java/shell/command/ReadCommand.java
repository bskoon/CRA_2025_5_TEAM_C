package shell.command;

import shell.Command;
import shell.Document;

public class ReadCommand implements Command {
    private static final int READ_ARG_COUNT = 2;
    private static final int FULLREAD_ARG_COUNT = 1;
    private static final int MAX_SSD_BLOCK = 100;

    private Document document;

    public ReadCommand (Document document) {
        this.document = document;
    }

    public boolean isArgumentValid(int argLength, boolean isFull, int lba) {
        if (!isValidParameter(argLength, isFull)) return false;
        if (!isValidLBA(lba)) return false;
        return true;
    }

    public boolean isValidParameter(int argLength, boolean isFull) {
        if (isFull) return argLength == FULLREAD_ARG_COUNT;
        return argLength == READ_ARG_COUNT;
    }

    public boolean isValidLBA(int lba) {
        return lba >= 0 && lba < MAX_SSD_BLOCK;
    }

    @Override
    public void execute(String[] args) {
        boolean isFull = args[0].equals("fullread");

        int lba = 0;
        int size = MAX_SSD_BLOCK;

        if (!isFull) {
            lba = Integer.parseInt(args[1]);
            size = 1;
        }

        if (!isArgumentValid(args.length, isFull, lba)) {
            System.out.println("INVALID COMMAND");
            return;
        }


        document.read(lba, size);
    }
}
