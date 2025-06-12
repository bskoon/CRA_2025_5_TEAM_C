package ssd.buffer;

import java.util.ArrayList;
import java.util.List;

public class CommandBuffer {
    private final List<String> buffer = new ArrayList<>();
    private final int maxSize = 10;

    private final BufferFileManager fileManager;

    public CommandBuffer(BufferFileManager fileManager) {
        this.fileManager = fileManager;
    }

    public void addCommand(String command) {
        buffer.add(command);
        if (buffer.size() >= maxSize) {
            flush();
        }
    }

    public void flush() {
        fileManager.saveCommands(buffer);
        buffer.clear();
    }

    public void loadBufferFromFile() {
        buffer.clear();
        buffer.addAll(fileManager.loadCommands());
    }

    public List<String> getBuffer() {
        return new ArrayList<>(buffer); // 외부에 복사본 제공
    }

    public boolean isEmpty() {
        return buffer.isEmpty();
    }
}
