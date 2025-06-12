package ssd.command;

import ssd.logic.SSDAppLogic;

public class WriteCommand implements Command {
    private final SSDAppLogic ssdAppLogic;

    public WriteCommand(SSDAppLogic logic) {
        this.ssdAppLogic = logic;
    }

    @Override
    public void execute(String[] args) {
        int lba = Integer.parseInt(args[1]);
        String data = args[2];
        ssdAppLogic.write(lba, data);
    }
}
