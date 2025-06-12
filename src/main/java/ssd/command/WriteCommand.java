package ssd.command;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.buffer.CommandBuffer;
import ssd.common.ValidCheck;

public class WriteCommand implements Command {
    private int lba;
    private String value;
    private CommandBuffer commandBuffer;

    public WriteCommand(CommandBuffer commandBuffer) {
        this.commandBuffer = commandBuffer;

    }

    @Override
    public void parameterCheck(String[] args) {
        if (args.length != 3) throw new RuntimeException("");
        if (!ValidCheck.isInRange0to99(args[1])) throw new RuntimeException("");
        if (!ValidCheck.isValidHex32(args[2])) throw new RuntimeException("");
    }

    @Override
    public void parameterSet(String[] args) {
        this.lba = Integer.parseInt(args[1]);
        this.value = args[2];
    }

    @Override
    public void execute(String[] args) {
        commandBuffer.write(lba, value);
    }
}
