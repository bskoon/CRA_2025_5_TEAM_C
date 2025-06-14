package shell.command;

public interface Command {
    boolean isVaildArgument(String[] args);
    void setArgument(String[] args);
    void execute(String[] args);
}
