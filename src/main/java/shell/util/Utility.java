package shell.util;

import shell.command.*;

import static shell.util.ShellConstant.*;

public class Utility {
    private static final Logger log = Logger.getLogger();
    private static Utility instance;

    public static Utility getInstance() {
        if (instance == null) instance = new Utility();
        return instance;
    }

    public CommandExecutor getCommandExecutor() {
        CommandLibrary commandLibrary = new CommandLibrary();
        Command readCommand = new ReadCommand(commandLibrary);
        Command writeCommand = new WriteCommand(commandLibrary);
        Command eraseCommand = new EraseCommand(commandLibrary);
        Command flushCommand = new FlushCommand(commandLibrary);
        Command scenarioCommand = new ScenarioCommand(commandLibrary);

        CommandExecutor executor = new CommandExecutor();
        executor.setCommand(READ, readCommand);
        executor.setCommand(WRITE, writeCommand);
        executor.setCommand(ERASE, eraseCommand);
        executor.setCommand(FULLREAD, readCommand);
        executor.setCommand(FULLWRITE, writeCommand);
        executor.setCommand(ERASERANGE, eraseCommand);
        executor.setCommand(FLUSH, flushCommand);
        executor.setCommand(SCENARIO_1, scenarioCommand);
        executor.setCommand(SCENARIO_2, scenarioCommand);
        executor.setCommand(SCENARIO_3, scenarioCommand);
        executor.setCommand(SCENARIO_4, scenarioCommand);
        return executor;
    }


    public String getExactCommand(String rawCommand) {
        String lowerCaseCommand = rawCommand.toLowerCase();
        return switch (lowerCaseCommand) {
            case "1_", SCENARIO_1 -> SCENARIO_1;
            case "2_", SCENARIO_2 -> SCENARIO_2;
            case "3_", SCENARIO_3 -> SCENARIO_3;
            case "4_", SCENARIO_4 -> SCENARIO_4;
            default -> lowerCaseCommand;
        };
    }

    public boolean isValidParameterCount(CommandType type, int argLength) {
        if (type == null) {
            log.log("Utility.isValidParameterCount()", "NULL CommandType");
            return false;
        }
        return type.getArgCount() == argLength;
    }

    public boolean isValidLBA(String lbaString) {
        try {
            int lba = Integer.parseInt(lbaString);
            return lba >= 0 && lba < MAX_SSD_BLOCK;
        } catch (NumberFormatException e) {
            log.log("Utility.isValidLBA()", "INVALID LBA");
        }
        return false;
    }

    public boolean isValidUpdateData(String updateData) {
        if (updateData.matches("0x[0-9A-F]{8}")) {
            return true;
        }
        else {
            log.log("Utility.isValidUpdateData()", "INVALID DATA");
        }
        return false;
    }
}
