package ssd.command;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.SSDConstant;
import ssd.buffer.CommandBuffer;
import ssd.common.ValidCheck;

public class EraseCommand implements Command {
    private int lba;
    private int size;
    private final CommandBuffer commandBuffer;


    public EraseCommand(CommandBuffer commandBuffer) {
        this.commandBuffer = commandBuffer;
    }

    @Override
    public void parameterCheck(String[] args) {
        if (args.length != 3) throw new RuntimeException("");
        if (!ValidCheck.isInRange0to99(args[1])) throw new RuntimeException("");
        if (!ValidCheck.isStringBetween0And10(args[2])) throw new RuntimeException("");
    }

    @Override
    public void parameterSet(String[] args) {
        this.lba = Integer.parseInt(args[1]);
        this.size = Integer.parseInt(args[2]);
        if (SSDConstant.MAX_LBA < lba + size) throw new RuntimeException("COMMAND ERROR");
    }

    @Override
    public void execute(String[] args) {
        commandBuffer.erase(lba,size);
    }
}
