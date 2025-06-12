package ssd.command;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private final Map<CommandType, Command> commandMap = new HashMap<>();

    public CommandExecutor(SSDIO ssdio, OutputIO outputIO) {
        commandMap.put(CommandType.R, new ReadCommand(ssdio, outputIO));
        commandMap.put(CommandType.W, new WriteCommand(ssdio, outputIO));
        commandMap.put(CommandType.E, new EraseCommand(ssdio, outputIO));
    }

    public void execute(CommandType commandKey, String[] args) {
        Command command = commandMap.get(commandKey);
        if (command != null) {
            command.parameterCheck(args);
            command.parameterSet(args);
            command.execute(args);
        } else {
            throw new RuntimeException("Unknown command: " + commandKey);
        }
    }
}
