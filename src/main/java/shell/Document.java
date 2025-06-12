package shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Document {
    private static final int MAX_LBA = 99;
    private static final String JAR_FILE_PATH = "SSD.jar";
    private static final List<String> EXECUTE_JAR = new ArrayList<>(List.of("java", "-jar", JAR_FILE_PATH));

    Random rand = new Random();

    public void read(int lba, boolean isFull) {
        int startLBA = 0;
        int endLBA = MAX_LBA;

        if (!isFull) {
            startLBA = lba;
            endLBA = lba;
        }

        for (int address = startLBA; address <= endLBA; address++) {
            String readVal = readLBA(address);
            System.out.println("LBA " + String.format("%02d", address) + ": " + readVal);
        }
    }

    public void write(int lba, String updateData, boolean isFull) {
        int startLBA = 0;
        int endLBA = MAX_LBA;

        if (!isFull) {
            startLBA = lba;
            endLBA = lba;
        }

        for (int address = startLBA; address <= endLBA; address++) {
            writeLBA(address, updateData);
        }
    }

    public void erase(int startLBA, int endLBA) {
        // ToDo. implement erase
    }

    public void scenario(int scenarioNum) {
        try {
            switch (scenarioNum) {
                case 1:
                    System.out.println(fullWriteAndReadCompare());
                    break;
                case 2:
                    System.out.println(partialLBAWrite());
                    break;
                case 3:
                    System.out.println(writeReadAging());
                    break;
                case 4:
                    System.out.println(eraseAndWriteAging());
                    break;
                default:

            }
        } catch (Exception e) {

        }
    }

    public void writeLBA(int lba, String hexValue) {
        callSsdWriteProcess(lba,hexValue);
    }

    public String readLBA(int lba) {
        return callSsdReadProcess(lba);
    }

    private void callSsdWriteProcess(int lba, String hexValue){
        // 실행할 명령어 인자를 설정
        List<String> executableCommand = generateCommand("W", lba, hexValue);
        callSSD(executableCommand);
    }

    private String callSsdReadProcess(int lba) {
        List<String> executableCommand = generateCommand("R", lba, "");
        callSSD(executableCommand);

        return readSSDDataFromOutputFile();  // 결과 반환;
    }

    private static List<String> generateCommand(String type, int lba, String hexValue) {
        List<String> executableCommand = new ArrayList<>(EXECUTE_JAR);

        String lbaString = Integer.toString(lba);

        executableCommand.add(type);
        executableCommand.add(lbaString);
        if (type.equals("W")) executableCommand.add(hexValue);

        return executableCommand;
    }

    private static void callSSD(List<String> executableCommand) {
        // ProcessBuilder 생성
        ProcessBuilder processBuilder = new ProcessBuilder(executableCommand);

        // 프로세스 실행
        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String readSSDDataFromOutputFile() {
        String result = "";
        try {
            // 파일 경로
            String filePath = "ssd_output.txt";
            result = readFileToString(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result.replace("\n","").trim();
    }

    public String readFileToString(String filePath) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get(filePath));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append(System.lineSeparator());  // 각 줄마다 줄바꿈 추가
        }

        reader.close();
        return stringBuilder.toString();
    }

    private String readCompare(int i, String s) throws IOException {
        String result = readLBA(i);
        if(!result.equals(s)) return "FAIL";
        return "PASS";
    }

    public String fullWriteAndReadCompare() throws IOException {
        for(int i=0;i<20;i++){
            for(int j=0;j<5;j++){
                String hexString = getRandomHexString(rand);
                writeLBA(i*5+j,hexString);
                String result = readCompare(i*5+j,hexString);
                if(result.equals("FAIL")) return result;
            }
        }
        return "PASS";
    }

    public String partialLBAWrite() throws IOException {
        for(int i=0;i<30;i++){
            writeLBA(4,"0xFFFFFFFF");
            writeLBA(0,"0xFFFFFFFF");
            writeLBA(3,"0xFFFFFFFF");
            writeLBA(1,"0xFFFFFFFF");
            writeLBA(2,"0xFFFFFFFF");
            for(int j=0;j<5;j++){
                String result = readCompare(j,"0xFFFFFFFF");
                if(result.equals("FAIL")) return result;
            }
        }
        return "PASS";
    }

    private String getRandomHexString(Random rand) {
        // 32비트 난수 생성 (0 ~ 0xFFFFFFFF)
        int randomValue = rand.nextInt(); // 기본적으로 -2^31 ~ 2^31-1 범위의 값
        // 16진수로 출력
        return "0x"+String.format("%08X", randomValue);
    }

    public String writeReadAging() throws IOException {
        for(int i=0;i<200;i++){
            if(!writeReadAgingOnce()) return "FAIL";
        }
        return "PASS";
    }

    public boolean writeReadAgingOnce() throws IOException {
        String hexString = getRandomHexString(rand);
        writeLBA(0, hexString);
        String result = readCompare(0,hexString);
        if(result.equals("FAIL")) return false;
        hexString = getRandomHexString(rand);
        writeLBA(99,hexString);
        result = readCompare(99,hexString);
        if(result.equals("FAIL")) return false;

        return true;
    }

    public String eraseAndWriteAging() {
        return "";
    }
}
