package ssd.logic;

import ssd.IO.OutputIO;
import ssd.IO.SSDIO;

import static ssd.SSDConstant.*;

public class SSDAppLogic {
    private final OutputIO outputIO;
    private final SSDIO ssdIO;

    public SSDAppLogic(OutputIO outputIO, SSDIO ssdIO) {
        this.outputIO = outputIO;
        this.ssdIO = ssdIO;
    }

    public void write(int lba, String newData) {
        try {
            checkWriteDataArgument(lba, newData);
            ssdIO.write(lba, newData);
        } catch (RuntimeException e) {
            outputIO.write(0, "ERROR");
        }
    }

    public void read(int lba) {
        try {
            checkLBARange(lba);
            String ssdData = ssdIO.read(lba);
            outputIO.write(0, ssdData);
        } catch (RuntimeException e) {
            outputIO.write(0, "ERROR");
        }
    }

    private void checkWriteDataArgument(int lba, String newData) {
        checkValidLBA(lba);
        checkDataStartWith0x(newData);
        checkDataLength(newData);
        checkValidData(newData);
    }

    private void checkValidLBA(int lba) {
        if (lba < MIN_LBA || lba > MAX_LBA) {
            throw new RuntimeException();
        }
    }

    private void checkDataStartWith0x(String newData) {
        if (!newData.startsWith("0x")) throw new RuntimeException();
    }

    private void checkDataLength(String newData) {
        if (newData.length() != 10) throw new RuntimeException();
    }

    private void checkValidData(String newData) {
        char[] strArr = newData.toCharArray();
        for (int j = 2; j < strArr.length; j++) {
            if (!((strArr[j] >= '0' && strArr[j] <= '9') || (strArr[j] >= 'A' && strArr[j] <= 'F')))
                throw new RuntimeException();
        }
    }

    private static void checkLBARange(int i) {
        if (i < 0 || i >= 100) {
            throw new RuntimeException("Error");
        }
    }
}
