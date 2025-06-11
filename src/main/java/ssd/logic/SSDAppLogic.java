package ssd.logic;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;

public class SSDAppLogic {
    private final OutputIO outputIO;
    private final SSDIO ssdIO;

    public SSDAppLogic(OutputIO outputIO, SSDIO ssdIO) {
        this.outputIO = outputIO;
        this.ssdIO = ssdIO;
    }

    public void handler(String command, int lba, String value) {
        // todo :: ssd read/write 기능 수행 및 에러 발생시 output IO 처리
    }
}
