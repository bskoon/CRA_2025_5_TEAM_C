package ssd.command;

public interface Command {
    void parameterCheck(String[] args);
    void parameterSet(String[] args);
    void execute(String[] args);
}
