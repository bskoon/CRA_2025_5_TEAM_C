package ssd.command;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.buffer.CommandBuffer;
import ssd.common.ValidCheck;

public class ReadCommand implements Command {
    private int lba;
    private CommandBuffer commandBuffer;

    public ReadCommand(CommandBuffer commandBuffer) {
        this.commandBuffer = commandBuffer;
    }

    @Override
    public void parameterCheck(String[] args) {
        if (args.length != 2) throw new RuntimeException("");
        if (!ValidCheck.isInRange0to99(args[1])) throw new RuntimeException("");
    }

    @Override
    public void parameterSet(String[] args) {
        this.lba = Integer.parseInt(args[1]);
    }

    @Override
    public void execute(String[] args) {
        commandBuffer.read(lba);
    }
}
