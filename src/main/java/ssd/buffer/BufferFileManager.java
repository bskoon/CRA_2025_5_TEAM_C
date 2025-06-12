package ssd.buffer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BufferFileManager {
    private final String filePath = "buffer_commands.txt";

    public void saveCommands(List<String> commands) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String command : commands) {
                writer.write(command);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("버퍼 저장 중 오류 발생: " + e.getMessage());
        }
    }

    public List<String> loadCommands() {
        List<String> commands = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) return commands;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                commands.add(line);
            }
        } catch (IOException e) {
            System.err.println("버퍼 불러오기 오류: " + e.getMessage());
        }

        return commands;
    }
}
