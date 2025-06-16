package shell.util;

public class ShellConstant {
    public static final String READ = "read";
    public static final String WRITE = "write";
    public static final String ERASE = "erase";
    public static final String FULLREAD = "fullread";
    public static final String FULLWRITE = "fullwrite";
    public static final String ERASERANGE = "erase_range";
    public static final String FLUSH = "flush";
    public static final String SCENARIO_1 = "1_fullwriteandreadcompare";
    public static final String SCENARIO_2 = "2_partiallbawrite";
    public static final String SCENARIO_3 = "3_writereadaging";
    public static final String SCENARIO_4 = "4_eraseandwriteaging";
    public static final String EXIT = "exit";
    public static final String HELP = "help";

    public static final int READ_ARG_COUNT = 2;
    public static final int FULLREAD_ARG_COUNT = 1;
    public static final int WRITE_ARG_COUNT = 3;
    public static final int FULLWRITE_ARG_COUNT = 2;
    public static final int ERASE_ARG_COUNT = 3;
    public static final int FLUSH_ARG_COUNT = 1;
    public static final int SCENARIO_ARG_COUNT = 1;

    public static final String READCOMMAND = "R";
    public static final String WRITECOMMAND = "W";
    public static final String ERASECOMMAND = "E";
    public static final String FLUSHCOMMAND = "F";

    public static final int MAX_SSD_BLOCK = 100;
}
