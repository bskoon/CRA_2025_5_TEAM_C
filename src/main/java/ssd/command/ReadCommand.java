package ssd.command;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.buffer.CommandBuffer;
import ssd.common.ValidCheck;

public class ReadCommand implements Command {
    private int lba;

    private final SSDIO ssdio;
    private final OutputIO outputIO;

    public ReadCommand(SSDIO ssdio, OutputIO outputIO) {
        this.ssdio = ssdio;
        this.outputIO = outputIO;
    }

    @Override
    public void execute(String[] args) {
        parameterCheck(args);
        parameterSet(args);
        String ssdData = ssdio.read(lba);
        outputIO.write(0, ssdData);
    }

    private void parameterCheck(String[] args) {
        if (args.length != 2) throw new RuntimeException("");
        if (!ValidCheck.isInRange0to99(args[1])) throw new RuntimeException("");
    }

    private void parameterSet(String[] args) {
        this.lba = Integer.parseInt(args[1]);
    }
}
