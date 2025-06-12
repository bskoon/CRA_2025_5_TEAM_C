package ssd.IO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandBufferIO extends IOHandler {
    public CommandBufferIO(String path) {
        super(path);
        createFolderIfNotExists(path);
        checkAndCreateFiles(path);
    }

    @Override
    public void write(int targetLine, String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("");
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    // 폴더가 없으면 폴더를 생성하는 함수
    public void createFolderIfNotExists(String folderPath) {
        Path path = Paths.get(folderPath);

        // 폴더가 존재하지 않으면 생성
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // '1_', '2_', '3_', '4_', '5_'로 시작하는 파일이 모두 있는지 확인하고 없으면 생성하는 함수
    public void checkAndCreateFiles(String directoryPath) {
        // 폴더 경로를 Path 객체로 변환
        Path dirPath = Paths.get(directoryPath);

        try {
            // 확인할 파일 접두어 배열
            String[] prefixes = {"1_", "2_", "3_", "4_", "5_"};
            boolean fileExists = true;

            // 각 접두어에 대해 파일이 있는지 확인
            for (String prefix : prefixes) {
                boolean exists = false;

                // 'prefix'로 시작하는 파일이 있는지 확인
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, prefix + "*")) {
                    for (Path entry : stream) {
                        if (Files.isRegularFile(entry)) {
                            exists = true;
                            break;
                        }
                    }
                }

                // 파일이 없다면 생성
                if (!exists) {
                    Path newFile = Paths.get(directoryPath, prefix + "empty.txt");
                    Files.createFile(newFile);
                    fileExists = false;  // 하나라도 생성되었으면 flag를 false로
                }
            }
        } catch (IOException e) {
            System.err.println("파일 작업 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

}
