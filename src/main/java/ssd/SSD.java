package ssd;

import ssd.IO.CommandBufferIO;
import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.logic.SSDAppLogic;
import ssd.logic.SSDCommandLogic;

import static ssd.SSDConstant.*;

public class SSD {
    public static void main(String[] args) {
        SSDIO ssdIO = new SSDIO(SSD_FILE_PATH);
        OutputIO outputIO = new OutputIO(OUTPUT_FILE_PATH);
        CommandBufferIO bufferIO = new CommandBufferIO(BUFFER_FOLDER_PATH);
        SSDAppLogic ssdAppLogic = new SSDAppLogic(outputIO, ssdIO);
        SSDCommandLogic ssdCommandLogic = new SSDCommandLogic(ssdAppLogic, outputIO, ssdIO);
        ssdCommandLogic.run(args);
    }
}
