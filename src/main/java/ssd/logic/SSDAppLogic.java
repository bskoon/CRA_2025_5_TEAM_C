package ssd.logic;

import ssd.IO.IOHandler;

import static ssd.SSDConstant.MAX_LBA;
import static ssd.SSDConstant.MIN_LBA;

public class SSDAppLogic {
    private final IOHandler outputIO;
    private final IOHandler ssdIO;

    public SSDAppLogic(IOHandler outputIO, IOHandler ssdIO) {
        this.outputIO = outputIO;
        this.ssdIO = ssdIO;
    }

    public void write(int lba, String newData) {
        try {
            checkValidWriteArgument(lba, newData);
            ssdIO.write(lba, newData);
        } catch (RuntimeException e) {
            outputIO.write(0, "ERROR");
        }
    }

    public void read(int lba) {
        try {
            checkValidLBA(lba);
            String ssdData = ssdIO.read(lba);
            outputIO.write(0, ssdData);
        } catch (RuntimeException e) {
            outputIO.write(0, "ERROR");
        }
    }

    private void checkValidWriteArgument(int lba, String newData) {
        checkValidLBA(lba);
        checkValidNewData(newData);
    }

    private void checkValidNewData(String newData) {
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
}
