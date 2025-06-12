package shell.command;

public class ScenarioCommand implements Command {
    private Document document;

    private static final int SCRIPT_ARG_COUNT = 1;

    public static final String SCRIPT_1 = "1_fullwriteandreadcompare";
    public static final String SCRIPT_2 = "2_partiallbawrite";
    public static final String SCRIPT_3 = "3_writereadaging";

    public ScenarioCommand (Document document) {
        this.document = document;
    }

    private int getScenarioNum(String scenarioName) {
        switch (scenarioName) {
            case SCRIPT_1:
                return 1;
            case SCRIPT_2:
                return 2;
            case SCRIPT_3:
                return 3;
            default:
                return 0;
        }
    }

    public boolean isValidParameter(int argLength) {
        return argLength == SCRIPT_ARG_COUNT;
    }

    @Override
    public void execute(String[] args) {
        if (!isValidParameter(args.length)) {
            System.out.println("INVALID COMMAND");
            return;
        }

        int scenarioNum = getScenarioNum(args[0]);
        document.scenario(scenarioNum);
    }
}
