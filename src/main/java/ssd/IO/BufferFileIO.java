package ssd.IO;

import java.io.*;

public class BufferFileIO extends IOHandler{

    public BufferFileIO(String filePath) {
        super(filePath);
    }

    @Override
    public void write(int targetLine, String newData) {
        try {
            File file = new File(path);
            file.delete();
            File newFile = new File(newData);
            newFile.createNewFile();
            path = newData;
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
