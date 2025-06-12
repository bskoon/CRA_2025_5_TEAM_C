package ssd.logic;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.buffer.CommandBuffer;
import ssd.command.CommandExecutor;
import ssd.command.CommandType;

public class SSDCommandLogic {
    private final CommandBuffer commandBuffer;

    public SSDCommandLogic(CommandBuffer commandBuffer) {
        this.commandBuffer = commandBuffer;
    }

    public void run(String[] args) {
        try {
            if (args.length == 0) throw new RuntimeException();
            CommandType commandType = CommandType.fromString(args[0]);

            CommandExecutor executor = new CommandExecutor(commandBuffer);
            executor.execute(commandType, args);
        } catch (RuntimeException e) {
            commandBuffer.errorWrite(0, "ERROR");
        }
    }
}
