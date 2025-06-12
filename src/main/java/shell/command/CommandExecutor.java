package shell.command;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private Map<String, Command> commandMap = new HashMap<>();

    public static final String SCRIPT_1 = "1_fullwriteandreadcompare";
    public static final String SCRIPT_2 = "2_partiallbawrite";
    public static final String SCRIPT_3 = "3_writereadaging";

    public void setCommand(String key, Command command) {
        commandMap.put(key, command);
    }

    private String getExactCommand(String rawCommand) {
        switch (rawCommand) {
            case "1_":
            case SCRIPT_1:
                return SCRIPT_1;
            case "2_":
            case SCRIPT_2:
                return SCRIPT_2;
            case "3_":
            case SCRIPT_3:
                return SCRIPT_3;
            default:
                return rawCommand;
        }
    }

    public void executeCommand(String[] args) {
        String key = args[0].toLowerCase();
        args[0] = getExactCommand(key);
        Command command = commandMap.get(args[0]);

        if (command != null) {
            command.execute(args);
        } else {
            System.out.println("Unknown command: " + key);
        }
    }
}
