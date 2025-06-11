package ssd;

public class OutputHandler implements IOHandler{
    private static String NAND_FILE_PATH = "ssd_nand.txt";
    private static String OUTPUT_FILE_PATH = "ssd_output.txt";

    public OutputHandler() {
        // todo :: 위의 파일(ssd_nand, ssd_output 존재하지 않을 시 생성)
    }

    @Override
    public void nandWrite(String value) {
        // todo :: 실제 NAND_FILE_PATH write 시에 해당 파일 기록 로직 개발

    }

    @Override
    public void outPutWrite(String value) {
        // todo :: 실제 OUTPUT_FILE_PATH read 완료 후 해당 파일 기록 로직 개발
    }
}
