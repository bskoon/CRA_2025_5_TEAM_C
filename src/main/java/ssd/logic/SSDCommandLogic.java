package ssd.logic;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.command.CommandExecutor;
import ssd.command.CommandType;

public class SSDCommandLogic {
    private final OutputIO outputIO;
    private final SSDIO ssdIO;

    public SSDCommandLogic(OutputIO outputIO, SSDIO ssdio) {
        this.outputIO = outputIO;
        this.ssdIO = ssdio;
    }

    public void run(String[] args) {
        try {
            if (args.length == 0) throw new RuntimeException();
            CommandType commandType = CommandType.fromString(args[0]);

            CommandExecutor executor = new CommandExecutor(ssdIO, outputIO);
            executor.execute(commandType, args);
        } catch (RuntimeException e) {
            outputIO.write(0, "ERROR");
        }
    }
}
