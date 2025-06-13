package ssd.command;

public class FlushCommand implements Command {
    public FlushCommand() {}

    @Override
    public void execute(String[] args) {
        parameterCheck(args);
        parameterSet(args);
        // todo :: flush 기능 구현
    }

    private void parameterCheck(String[] args) {

    }

    private void parameterSet(String[] args) {

    }
}
