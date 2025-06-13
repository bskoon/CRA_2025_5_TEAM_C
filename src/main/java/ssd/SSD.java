package ssd;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.logic.SSDCommandLogic;

import static ssd.SSDConstant.OUTPUT_FILE_PATH;
import static ssd.SSDConstant.SSD_FILE_PATH;

public class SSD {
    public static void main(String[] args) {
        SSDIO ssdio = new SSDIO(SSD_FILE_PATH);
        OutputIO outputIO = new OutputIO(OUTPUT_FILE_PATH);

        SSDCommandLogic ssdCommandLogic = new SSDCommandLogic(ssdio, outputIO);
        ssdCommandLogic.run(args);
    }
}
