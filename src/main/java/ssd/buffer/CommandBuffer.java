package ssd.buffer;

import ssd.IO.BufferFileIO;
import ssd.command.CommandExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ssd.SSDConstant.BUFFER_FOLDER_PATH;

public class CommandBuffer {
    // 확인할 접두어 배열
    private final String[] prefixes = {"1_", "2_", "3_", "4_", "5_"};
    private final List<String> buffer = new ArrayList<>();
    private final int maxSize = 5;

    private final List<BufferFileIO> fileManager;
    private final CommandExecutor commandExecutor;
    private final SSDArgument ssdArgument;

    public CommandBuffer(CommandExecutor executor, SSDArgument ssdArgument) {
        File logDir = new File(BUFFER_FOLDER_PATH);
        if (!logDir.exists()) logDir.mkdirs();
        fileManager = new ArrayList<>();
        checkFilesOrCreateEmpty(BUFFER_FOLDER_PATH);

        this.commandExecutor = executor;
        this.ssdArgument = ssdArgument;
    }

    public void bufferExecutor() {
        ssdArgument.toString();
    }

    public List<String> checkFilesOrCreateEmpty(String directoryPath) {
        List<String> fileList = new ArrayList<>();
        String[] prefixes = {"1_", "2_", "3_", "4_", "5_"};
        Path dirPath = Paths.get(directoryPath);

        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath); // 디렉토리 없으면 생성
            }

            for (String prefix : prefixes) {
                boolean found = false;

                // 디렉토리 내에서 prefix로 시작하는 파일을 검색
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, prefix + "*")) {
                    for (Path entry : stream) {
                        if (Files.isRegularFile(entry)) {
                            fileManager.add(new BufferFileIO(entry.getFileName().toString()));  // 찾은 파일명 추가
                            found = true;
                            break; // 하나만 존재해야 하므로 바로 break
                        }
                    }
                }

                // 해당 prefix로 시작하는 파일이 없다면 empty.txt 파일 생성
                if (!found) {
                    String emptyFileName = prefix + "empty.txt";
                    Path emptyFilePath = dirPath.resolve(emptyFileName);
                    Files.createFile(emptyFilePath);
                    fileManager.add(new BufferFileIO(emptyFileName));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("");
        }

        return fileList;
    }

    public void addCommand(String command) {
        buffer.add(command);
        if (buffer.size() >= maxSize) {
            flush();
        }
    }

    public void flush() {
        // todo: ssd flush 기능 추가

        // 파일 + 내부 캐싱 데이터 초기화
        for (int i = 1; i <= 5; i++) {
            fileManager.get(i).write(0, BUFFER_FOLDER_PATH + "/" + i + "_empty");
        }
        buffer.clear();
    }

    public void loadBufferFromFile() {
        buffer.clear();
        for (BufferFileIO file : fileManager) {
            buffer.add(file.loadCommands());
        }
    }

    public List<String> getBuffer() {
        return new ArrayList<>(buffer); // 외부에 복사본 제공
    }

    public boolean isEmpty() {
        return buffer.isEmpty();
    }


    public void errorWrite(int i, String error) {
    }

    public void flush(List<String> commandList) {
        Collections.sort(commandList);

        for (String commandData : commandList) {
            String[] original = commandData.split("_");
            String[] trimmed = Arrays.copyOfRange(original, 1, original.length);
            commandExecutor.execute(trimmed);
        }
    }
}
