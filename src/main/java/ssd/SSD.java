package ssd;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.buffer.CommandBuffer;
import ssd.logic.SSDCommandLogic;

import static ssd.SSDConstant.OUTPUT_FILE_PATH;
import static ssd.SSDConstant.SSD_FILE_PATH;

public class SSD {
    public static void main(String[] args) {
        SSDIO ssdIO = new SSDIO(SSD_FILE_PATH);
        OutputIO outputIO = new OutputIO(OUTPUT_FILE_PATH);
        CommandBuffer commandBuffer = new CommandBuffer(ssdIO,outputIO);
        SSDCommandLogic ssdCommandLogic = new SSDCommandLogic(commandBuffer);
        ssdCommandLogic.run(args);
    }
}
