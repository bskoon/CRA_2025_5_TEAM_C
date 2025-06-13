package ssd.logic;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.buffer.CommandBuffer;
import ssd.buffer.SSDArgument;
import ssd.command.CommandExecutor;

public class SSDCommandLogic {
    private final SSDIO ssdio;
    private final OutputIO outputIO;

    public SSDCommandLogic(SSDIO ssdio, OutputIO outputIO) {
        this.ssdio = ssdio;
        this.outputIO = outputIO;
    }

    public void run(String[] args) {
        try {
            SSDArgument ssdArgument = new SSDArgument(args);
            CommandExecutor executor = new CommandExecutor(ssdio, outputIO);

            CommandBuffer commandBuffer = new CommandBuffer(executor, ssdArgument);
            commandBuffer.bufferExecutor();
        } catch (RuntimeException e) {
            outputIO.write(0, "ERROR");
        }
    }
}
