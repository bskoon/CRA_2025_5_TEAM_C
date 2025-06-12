package shell.command;

import shell.Command;
import shell.Document;

public class ScenarioCommand implements Command {
    private Document document;

    public ScenarioCommand (Document document) {
        this.document = document;
    }

    @Override
    public void execute(String[] args) {
        int scenarioNum = Integer.parseInt(args[1]);
        document.scenario(scenarioNum);
    }
}
