package ssd.command;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.buffer.CommandBuffer;
import ssd.common.ValidCheck;

public class WriteCommand implements Command {
    private int lba;
    private String value;

    private final SSDIO ssdio;
    private final OutputIO outputIO;

    public WriteCommand(SSDIO ssdio, OutputIO outputIO) {
        this.ssdio = ssdio;
        this.outputIO = outputIO;
    }

    @Override
    public void execute(String[] args) {
        parameterCheck(args);
        parameterSet(args);
        ssdio.write(lba, value);
    }

    private void parameterCheck(String[] args) {
        if (args.length != 3) throw new RuntimeException("");
        if (!ValidCheck.isInRange0to99(args[1])) throw new RuntimeException("");
        if (!ValidCheck.isValidHex32(args[2])) throw new RuntimeException("");
    }

    private void parameterSet(String[] args) {
        this.lba = Integer.parseInt(args[1]);
        this.value = args[2];
    }
}
