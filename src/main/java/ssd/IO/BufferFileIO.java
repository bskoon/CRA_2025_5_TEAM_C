package ssd.IO;

import java.io.*;

import static ssd.common.SSDConstant.BUFFER_FOLDER_PATH;

public class BufferFileIO extends IOHandler{

    public BufferFileIO(String filePath) {
        super(BUFFER_FOLDER_PATH+"/"+filePath);
    }

    @Override
    public void write(int targetLine, String newData) {
        try {
            File file = new File(path);
            file.delete();
            File newFile = new File(BUFFER_FOLDER_PATH+"/"+newData);
            newFile.createNewFile();
            path = BUFFER_FOLDER_PATH+"/"+newData;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String loadCommands() {
        if(path.contains("empty")) return null;
        File file = new File(path);
        return file.getName();
    }
}
