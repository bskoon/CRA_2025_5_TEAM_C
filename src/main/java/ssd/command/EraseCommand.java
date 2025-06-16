package ssd.command;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.common.SSDConstant;
import ssd.common.ValidCheck;

public class EraseCommand implements Command {
    private int lba;
    private int size;

    private final SSDIO ssdio;
    private final OutputIO outputIO;

    public EraseCommand(SSDIO ssdio, OutputIO outputIO) {
        this.ssdio = ssdio;
        this.outputIO = outputIO;
    }

    @Override
    public void execute(String[] args) {
        parameterCheck(args);
        parameterSet(args);

        try {
            for (int i = 0; i < size; i++) {
                ssdio.write(lba + i, "0x00000000");
            }
        } catch (RuntimeException e) {
            outputIO.write(0, "ERROR");
        }
    }

    private void parameterCheck(String[] args) {
        if (args.length != 3) throw new RuntimeException("");
        if (!ValidCheck.isInRange0to99(args[1])) throw new RuntimeException("");
        if (!ValidCheck.isStringBetween0And10(args[2])) throw new RuntimeException("");
    }

    private void parameterSet(String[] args) {
        this.lba = Integer.parseInt(args[1]);
        this.size = Integer.parseInt(args[2]);
        if (SSDConstant.MAX_LBA < lba + size - 1) throw new RuntimeException("COMMAND ERROR");
    }
}
