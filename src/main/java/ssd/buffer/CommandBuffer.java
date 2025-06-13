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
    private final List<String> buffer = new ArrayList<>();
    private final int MAX_SIZE = 5;

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
                // 버퍼 갱신
                loadBufferFromFile();
                if(buffer.size() >= MAX_SIZE){
                    flush();
                }
                fileManager.get(buffer.size()).write(0,BUFFER_FOLDER_PATH+"/"+ssdArgument.makeFileName(buffer.size()+1));
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

    private Optional<String> readBuffer(int lba) {loadBufferFromFile();
        List<String> bufferList = new ArrayList<>();
        for(String command : buffer){
            String fixedCommand = command.replace(".txt","");
            bufferList.add(fixedCommand);
        }

        return CommandBufferOptimizer.fastRead(bufferList,lba);
    }

    private void rewriteBuffer() {
        loadBufferFromFile();
        List<String> bufferList = new ArrayList<>();
        for(String command : buffer){
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
                            fileManager.add(new BufferFileIO(BUFFER_FOLDER_PATH+"/"+entry.getFileName().toString()));  // 찾은 파일명 추가
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
                    fileManager.add(new BufferFileIO(BUFFER_FOLDER_PATH+"/"+emptyFileName));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("");
        }

        return fileList;
    }

    public void flush() {
        // todo: ssd flush 기능 추가
        loadBufferFromFile();
        for(String commands: buffer){
            SSDArgument convertedCommand = new SSDArgument(commands.replace(".txt","").substring(2).split("_"));
            commandExecutor.execute(convertedCommand.getArgs());
        }

        // 파일 + 내부 캐싱 데이터 초기화
        for (int i = 0; i < MAX_SIZE; i++) {
            fileManager.get(i).write(0, BUFFER_FOLDER_PATH + "/" + (i+1) + "_empty.txt");
        }
        buffer.clear();
    }

    public void loadBufferFromFile() {
        buffer.clear();
        for (BufferFileIO file : fileManager) {
            if(file.loadCommands()!=null)buffer.add(file.loadCommands());
        }
    }

    public List<String> getBuffer() {
        return new ArrayList<>(buffer); // 외부에 복사본 제공
    }
}
