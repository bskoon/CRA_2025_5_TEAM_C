package ssd.command;

import ssd.logic.SSDAppLogic;

public class EraseCommand implements Command {
    private final SSDAppLogic ssdAppLogic;

    public EraseCommand(SSDAppLogic logic) {
        this.ssdAppLogic = logic;
    }

    @Override
    public void execute(String[] args) {
        // todo :: erase 기능 구현
    }
}
