package shell.command;

import shell.util.Utility;

public class EraseCommand implements Command {
    private Document document;
    Utility util;

    public EraseCommand (Document document) {
        this.document = document;
        this.util = Utility.getLogger();
    }

    @Override
    public boolean argumentCheck(String[] args) {

    }

    @Override
    public void setArgument(String[] args) {

    }

    @Override
    public void execute(String[] args) {
        int lba;
        int size;
        if (isEraseRange(args)) {
            int startLBA = Integer.parseInt(args[1]);
            int endLBA = Integer.parseInt(args[2]);
            if (!isValidLBA(startLBA) || !isValidLBA(endLBA)) {
                System.out.println("INVALID COMMAND");
                return;
            }
            lba = startLBA;
            size = endLBA - startLBA + 1;
        } else { // erase
            lba = Integer.parseInt(args[1]);
            size = Integer.parseInt(args[2]);
            if (!isValidLBA(lba)) {
                System.out.println("INVALID COMMAND");
                return;
            }
        }
        size = adjustSizeWithinBounds(lba, size);
        performEraseInChunks(lba, size);
    }

    private boolean isEraseRange(String[] args) {
        return args[0].equalsIgnoreCase("erase_range");
    }

    private int adjustSizeWithinBounds(int lba, int size) {
        int maxLbaLimit = 100;
        return Math.min(size, maxLbaLimit - lba);
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
