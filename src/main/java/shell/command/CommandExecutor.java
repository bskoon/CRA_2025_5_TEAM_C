package shell.command;

import shell.util.Utility;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private Map<String, Command> commandMap = new HashMap<>();
    private Utility util = Utility.getLogger();

    public void setCommand(String key, Command command) {
        commandMap.put(key, command);
    }

    public void executeCommand(String[] args) {
        args[0] = util.getExactCommand(args[0]);
        CommandType type = CommandType.fromString(args[0]);
        if (!util.isValidParameterCount(type, args.length)) {
            System.out.println("INVALID COMMAND");
            return;
        }

        Command command = commandMap.get(args[0]);

        if (command != null) {
            command.execute(args);
        } else {
            System.out.println("Unknown command: " + args[0]);
        }
    }
}
