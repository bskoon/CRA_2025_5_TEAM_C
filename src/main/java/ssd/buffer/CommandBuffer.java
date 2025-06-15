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

import static ssd.SSDConstant.BUFFER_FOLDER_PATH;

public class CommandBuffer {
    private final int MAX_SIZE = 5;

    private List<BufferFileIO> fileManager;
    private CommandExecutor commandExecutor;
    private SSDArgument ssdArgument;

    public CommandBuffer(CommandExecutor executor, SSDArgument ssdArgument) {
        File logDir = new File(BUFFER_FOLDER_PATH);
        if (!logDir.exists()) logDir.mkdirs();

        checkFilesOrCreateEmpty(BUFFER_FOLDER_PATH);

        this.commandExecutor = executor;
        this.ssdArgument = ssdArgument;
    }

    public void bufferExecutor() {
        switch (ssdArgument.getCommand()){
            case "R":
                //버퍼에서 읽어서 값 있는지 체크
                Optional<String> bufferData =readBuffer(ssdArgument.getLba());
                if(bufferData.isPresent()){
                    commandExecutor.getOutputIO().write(0,bufferData.get());
                }else{
                    // 없을때
                    commandExecutor.execute(ssdArgument.getArgs());
                }
                break;
            case "W":
            case "E":
                if(loadBufferFromFile().size() >= MAX_SIZE){
                    flush();
                }
                int index = loadBufferFromFile().size();
                fileManager.get(index).write(0,BUFFER_FOLDER_PATH+"/"+ssdArgument.makeFileName(index+1));
                // 버퍼 최적화 알고리즘 돌리기.?
                rewriteBuffer();
                break;
            case "F":
                flush();
                break;
            default:
                break;
        }
    }

    private Optional<String> readBuffer(int lba) {
        List<String> bufferList = new ArrayList<>();
        for(String command : loadBufferFromFile()){
            String fixedCommand = command.replace(".txt","");
            bufferList.add(fixedCommand);
        }

        return CommandBufferOptimizer.fastRead(bufferList,lba);
    }

    private void rewriteBuffer() {
        List<String> bufferList = new ArrayList<>();
        for(String command : loadBufferFromFile()){
            String fixedCommand = command.replace(".txt","");
            bufferList.add(fixedCommand);
        }

        List<String> result = CommandBufferOptimizer.optimize(bufferList);

        for(int i=0;i<MAX_SIZE;i++){
            BufferFileIO io = fileManager.get(i);
            if(result.size()>i){
               io.write(0,BUFFER_FOLDER_PATH+"/"+result.get(i)+".txt");
            }else{
               io.write(0,BUFFER_FOLDER_PATH+"/"+(i+1)+"_empty.txt");
            }
        }

    }

    private void checkFilesOrCreateEmpty(String directoryPath) {
        fileManager = new ArrayList<>();

        String[] prefixes = {"1_", "2_", "3_", "4_", "5_"};
        Path dirPath = Paths.get(directoryPath);

        try {
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

    }

    public void flush() {
        // ssd flush 기능 추가
        for(String commands: loadBufferFromFile()){
            SSDArgument convertedCommand = new SSDArgument(commands);
            commandExecutor.execute(convertedCommand.getArgs());
        }

        // 파일 + 내부 캐싱 데이터 초기화
        for (int i = 0; i < MAX_SIZE; i++) {
            fileManager.get(i).write(0, BUFFER_FOLDER_PATH + "/" + (i+1) + "_empty.txt");
        }
    }

    private List<String> loadBufferFromFile() {
        List<String> buffer = new ArrayList<>();
        for (BufferFileIO file : fileManager) {
            if(file.loadCommands()!=null)buffer.add(file.loadCommands());
        }

        return buffer;
    }

    public void setSsdArgument(SSDArgument ssdArgument){
        this.ssdArgument = ssdArgument;
    }
}
