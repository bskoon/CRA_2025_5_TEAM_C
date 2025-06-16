package ssd.buffer;

import ssd.IO.BufferFileIO;
import ssd.command.CommandBufferOptimizer;
import ssd.command.CommandExecutor;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static ssd.common.SSDConstant.*;
import static ssd.common.SSDConstant.BUFFER_FOLDER_PATH;

public class CommandBuffer {
    private final int MAX_SIZE = 5;

    private List<BufferFileIO> fileManager;
    private CommandExecutor commandExecutor;
    private SSDArgument ssdArgument;

    public CommandBuffer(CommandExecutor executor, SSDArgument ssdArgument) {
        foundOrCreateCommand();

        this.commandExecutor = executor;
        this.ssdArgument = ssdArgument;
    }

    public void bufferExecutor() {
        switch (ssdArgument.getCommand()){
            case READ:
                readProcess();
                break;
            case WRITE:
            case ERASE:
                writeOrEraseProcess();
                break;
            case FLUSH:
                flushProcess();
                break;
            default:
                break;
        }
    }

    private void readProcess() {
        Optional<String> bufferData = fastRead(ssdArgument.getLba());
        if(bufferData.isPresent()){
            commandExecutor.getOutputIO().write(0,bufferData.get());
        }else{
            commandExecutor.execute(ssdArgument.getArgs());
        }
    }

    private Optional<String> fastRead(int lba) {
        List<String> bufferList = makeBufferList();

        return CommandBufferOptimizer.fastRead(bufferList,lba);
    }

    private void writeOrEraseProcess() {
        if(currentBufferStatusList().size() >= MAX_SIZE){
            flushProcess();
        }
        int lastBufferIndex = currentBufferStatusList().size();
        fileManager.get(lastBufferIndex).write(0,ssdArgument.makeFileName(lastBufferIndex+1));
        // 버퍼 최적화 알고리즘 돌리기.?
        rewriteBuffer();
    }

    private void rewriteBuffer() {
        List<String> result = CommandBufferOptimizer.optimize(makeBufferList());

        for(int i=0;i<MAX_SIZE;i++){
            if(result.size()>i){
                fileManager.get(i).write(0,result.get(i)+".txt");
            }else{
                fileManager.get(i).write(0,(i+1)+"_empty.txt");
            }
        }

    }

    public void flushProcess() {
        // ssd flush 기능 추가
        for(String commands: currentBufferStatusList()){
            SSDArgument convertedCommand = new SSDArgument(commands);
            commandExecutor.execute(convertedCommand.getArgs());
        }

        // 파일 + 내부 캐싱 데이터 초기화
        for (int i = 0; i < MAX_SIZE; i++) {
            fileManager.get(i).write(0, (i+1) + "_empty.txt");
        }
    }

    private void foundOrCreateCommand() {
        fileManager = new ArrayList<>();
        String[] prefixes = {"1_", "2_", "3_", "4_", "5_"};

        File logDir = new File(BUFFER_FOLDER_PATH);
        if (!logDir.exists()) logDir.mkdirs();
        try {
            for (String prefix : prefixes) {
                boolean found = foundCommandFile(prefix);
                // 해당 prefix로 시작하는 파일이 없다면 empty.txt 파일 생성
                if (!found) makeEmptyCommandFile(prefix);
            }

        } catch (IOException e) {
            throw new RuntimeException("");
        }

    }

    private boolean foundCommandFile(String prefix) throws IOException {
        Path dirPath = Paths.get(BUFFER_FOLDER_PATH);

        DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, prefix + "*");
        for (Path entry : stream) {
            if (Files.isRegularFile(entry)) {
                fileManager.add(new BufferFileIO(entry.getFileName().toString()));  // 찾은 파일명 추가
                return true;
            }
        }
        return false;
    }

    private void makeEmptyCommandFile(String prefix) throws IOException {
        Path dirPath = Paths.get(BUFFER_FOLDER_PATH);

        String emptyFileName = prefix + "empty.txt";
        Path emptyFilePath = dirPath.resolve(emptyFileName);
        Files.createFile(emptyFilePath);
        fileManager.add(new BufferFileIO(emptyFileName));
    }

    private List<String> makeBufferList() {
        List<String> bufferList = new ArrayList<>();
        for(String command : currentBufferStatusList()){
            String fixedCommand = command.replace(".txt","");
            bufferList.add(fixedCommand);
        }
        return bufferList;
    }

    private List<String> currentBufferStatusList() {
        List<String> buffer = new ArrayList<>();
        for (BufferFileIO file : fileManager) {
            String command = file.loadCommands();
            if(command !=null) buffer.add(command);
        }

        return buffer;
    }

    public void setSsdArgument(SSDArgument ssdArgument){
        this.ssdArgument = ssdArgument;
    }
}
