package ssd.command;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.SSDConstant;
import ssd.logic.SSDAppLogic;

public class EraseCommand implements Command {
    private final SSDAppLogic ssdAppLogic;
    private final SSDIO ssdio;
    private final OutputIO outputIO;

    public EraseCommand(SSDAppLogic logic, SSDIO ssdio, OutputIO outputIO) {
        this.ssdAppLogic = logic;
        this.ssdio = ssdio;
        this.outputIO = outputIO;
    }

    @Override
    public void execute(String[] args) {
        try {
            int lba = Integer.parseInt(args[1]);
            int size = Integer.parseInt(args[2]);

            validCheck(lba,size);

            for(int i=0;i<size;i++){
                ssdio.write(lba+i, "0x00000000");
            }
        } catch (RuntimeException e) {
            outputIO.write(0, "ERROR");
        }
    }

    private void validCheck(int lba, int size) {
        if(SSDConstant.MAX_LBA<lba+size){
            throw new RuntimeException("COMMAND ERROR");
        }
    }
}
