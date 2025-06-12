package ssd.command;

import ssd.logic.SSDAppLogic;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private final Map<Character, Command> commandMap = new HashMap<>();

    public CommandExecutor(SSDAppLogic logic) {
        commandMap.put('R', new ReadCommand(logic));
        commandMap.put('W', new WriteCommand(logic));
        commandMap.put('E', new EraseCommand(logic));
    }

    public void execute(char commandKey, String[] args) {
        Command command = commandMap.get(commandKey);
        if (command != null) {
            command.execute(args);
        } else {
            throw new RuntimeException("Unknown command: " + commandKey);
        }
    }
}
