package ssd;

import ssd.IO.IOHandler;
import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.logic.SSDAppLogic;
import ssd.logic.SSDCommandLogic;

import static ssd.SSDConstant.*;

public class SSD {
    public static void main(String[] args) {
        IOHandler ssdIO = new SSDIO(SSD_FILE_PATH);
        IOHandler outputIO = new OutputIO(OUTPUT_FILE_PATH);
        SSDAppLogic ssdAppLogic = new SSDAppLogic(outputIO, ssdIO);
        SSDCommandLogic ssdCommandLogic = new SSDCommandLogic(ssdAppLogic, outputIO);
        ssdCommandLogic.run(args);
    }
}
