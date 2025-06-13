package shell.command;

public interface Command {
    boolean argumentCheck(String[] args);
    void setArgument(String[] args);
    void execute(String[] args);
}
