package ssd.command;

import ssd.logic.SSDAppLogic;

public class FlushCommand implements Command {
    private final SSDAppLogic ssdAppLogic;

    public FlushCommand(SSDAppLogic logic) {
        this.ssdAppLogic = logic;
    }

    @Override
    public void execute(String[] args) {
        // todo :: flush 기능 구현
    }
}
