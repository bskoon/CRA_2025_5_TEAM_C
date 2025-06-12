package ssd.command;

import ssd.logic.SSDAppLogic;

public class ReadCommand implements Command {
    private final SSDAppLogic ssdAppLogic;

    public ReadCommand(SSDAppLogic logic) {
        this.ssdAppLogic = logic;
    }

    @Override
    public void execute(String[] args) {
        int lba = Integer.parseInt(args[1]);
        ssdAppLogic.read(lba);
    }
}
