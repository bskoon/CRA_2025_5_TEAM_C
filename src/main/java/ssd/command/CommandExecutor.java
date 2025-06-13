package ssd.command;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.buffer.CommandBuffer;
import ssd.common.ValidCheck;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private final Map<String, Command> commandMap = new HashMap<>();

    public CommandExecutor(SSDIO ssdio, OutputIO outputIO) {
        commandMap.put("R", new ReadCommand(ssdio, outputIO));
        commandMap.put("W", new WriteCommand(ssdio, outputIO));
        commandMap.put("E", new EraseCommand(ssdio, outputIO));
    }

    public void execute(String[] args) {
        if (args.length == 0) throw new RuntimeException();
        ValidCheck.validateCommandType(args[0]);

        Command command = commandMap.get(args[0]);
        if (command == null)
            throw new RuntimeException("Unknown command: " + args[0]);

        command.execute(args);
    }
}
