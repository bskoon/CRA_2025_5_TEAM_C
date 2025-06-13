package shell.util;

import shell.command.CommandType;
import static shell.util.ShellConstant.*;

public class Utility {
    private static Utility instance;

    public static Utility getInstance() {
        if (instance == null) instance = new Utility();
        return instance;
    }


    public String getExactCommand(String rawCommand) {
        String lowerCaseCommand = rawCommand.toLowerCase();
        switch (lowerCaseCommand) {
            case "1_":
            case SCENARIO_1:
                return SCENARIO_1;
            case "2_":
            case SCENARIO_2:
                return SCENARIO_2;
            case "3_":
            case SCENARIO_3:
                return SCENARIO_3;
            case "4_":
            case SCENARIO_4:
                return SCENARIO_4;
            default:
                return lowerCaseCommand;
        }
    }

    public boolean isValidParameterCount(CommandType type, int argLength) {
        int correctLength = 0;
        switch (type) {
            case read:
                correctLength = READ_ARG_COUNT;
                break;
            case write:
                correctLength = WRITE_ARG_COUNT;
                break;
            case fullread:
                correctLength = FULLREAD_ARG_COUNT;
                break;
            case fullwrite:
                correctLength = FULLWRITE_ARG_COUNT;
                break;
            case erase:
            case erase_range:
                correctLength = ERASE_ARG_COUNT;
                break;
            case flush:
                correctLength = FLUSH_ARG_COUNT;
                break;
            case script1:
            case script2:
            case script3:
            case script4:
                correctLength = SCRIPT_ARG_COUNT;
                break;
            default:
                break;
        }
        return correctLength == argLength;
    }

    public boolean isValidLBA(String lbaString) {
        int lba;
        try {
            lba = Integer.parseInt(lbaString);
        } catch (NumberFormatException e) {
            return false;
        }

        return lba >= 0 && lba < MAX_SSD_BLOCK;
    }
    public boolean isValidUpdateData(String updateData) {
        return updateData.matches("0x[0-9A-F]{8}");
    }
}
