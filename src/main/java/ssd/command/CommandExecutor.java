package ssd.command;

import ssd.buffer.CommandBuffer;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private final Map<CommandType, Command> commandMap = new HashMap<>();

    public CommandExecutor(CommandBuffer commandBuffer) {
        commandMap.put(CommandType.R, new ReadCommand(commandBuffer));
        commandMap.put(CommandType.W, new WriteCommand(commandBuffer));
        commandMap.put(CommandType.E, new EraseCommand(commandBuffer));
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
