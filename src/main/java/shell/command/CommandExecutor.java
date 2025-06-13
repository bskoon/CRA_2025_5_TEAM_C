package shell.command;

import shell.util.Logger;
import shell.util.Utility;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private static final Logger log = Logger.getLogger();

    private Map<String, Command> commandMap = new HashMap<>();
    private Utility util = Utility.getInstance();

    public void setCommand(String key, Command command) {
        commandMap.put(key, command);
    }

    public void executeCommand(String[] args) {
        try {
            args[0] = util.getExactCommand(args[0]);
            CommandType type = CommandType.fromString(args[0]);
            if (!util.isValidParameterCount(type, args.length)) {
                log.print("INVALID COMMAND");
                return;
            }
            Command command = commandMap.get(args[0]);

            if (command != null) {
                command.execute(args);
            } else {
                log.log("CommandExecutor.executeCommand()", "INVALID COMMAND");
                log.print("Unknown command: " + args[0]);
            }
        } catch (Exception e) {
            log.log("CommandExecutor.executeCommand()", "Exception Occur on executeCommand()");
        }
    }
}
