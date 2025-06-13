package shell.util;

import shell.command.CommandType;

public class Utility {
    private static final Logger log = Logger.getLogger();
    private static Utility instance;

    public static Utility getInstance() {
        if (instance == null) instance = new Utility();
        return instance;
    }

    private static final int READ_ARG_COUNT = 2;
    private static final int FULLREAD_ARG_COUNT = 1;
    private static final int WRITE_ARG_COUNT = 3;
    private static final int FULLWRITE_ARG_COUNT = 2;
    private static final int ERASE_ARG_COUNT = 3;
    private static final int FLUSH_ARG_COUNT = 1;
    private static final int SCRIPT_ARG_COUNT = 1;

    public static final String SCRIPT_1 = "1_fullwriteandreadcompare";
    public static final String SCRIPT_2 = "2_partiallbawrite";
    public static final String SCRIPT_3 = "3_writereadaging";
    public static final String SCRIPT_4 = "4_eraseandwriteaging";

    public static final int MAX_SSD_BLOCK = 100;

    public String getExactCommand(String rawCommand) {
        String lowerCaseCommand = rawCommand.toLowerCase();
        return switch (lowerCaseCommand) {
            case "1_", SCRIPT_1 -> SCRIPT_1;
            case "2_", SCRIPT_2 -> SCRIPT_2;
            case "3_", SCRIPT_3 -> SCRIPT_3;
            case "4_", SCRIPT_4 -> SCRIPT_4;
            default -> lowerCaseCommand;
        };
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
                log.log("Utility.isValidParameterCount()", "INVALID PARAMETER");
                break;
        }
        return correctLength == argLength;
    }

    public boolean isValidLBA(String lbaString) {
        int lba;
        try {
            lba = Integer.parseInt(lbaString);
        } catch (NumberFormatException e) {
            log.log("Utility.isValidLBA()", "INVALID LBA");
            return false;
        }

        return lba >= 0 && lba < MAX_SSD_BLOCK;
    }
    public boolean isValidUpdateData(String updateData) {
        if (updateData.matches("0x[0-9A-F]{8}")) return true;
        else {
            log.log("Utility.isValidUpdateData()", "INVALID DATA");
            return false;
        }
    }
}
