package shell;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private Map<String, Command> commandMap = new HashMap<>();

    public void setCommand(String key, Command command) {
        commandMap.put(key, command);
    }

    public void executeCommand(String[] args) {
        String key = "";
        Command command = commandMap.get(key);
        if (command != null) {
            command.execute(args);
        } else {
            System.out.println("Unknown command: " + key);
        }
    }
}
