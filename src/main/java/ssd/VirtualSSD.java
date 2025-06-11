package ssd;

public class VirtualSSD {
    private final IOHandler ioHandler;

    public VirtualSSD(IOHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    public void write(int lba, String value) {
        // todo :: SSD write 로직 여기로 이동
        // todo :: write 로직 구현 시 IOHandler 사용하여 기록
    }

    public String read(int lba) {
        // todo :: SSD read 로직 여기로 이동
        // todo :: read 로직 구현 시 IOHandler 사용하여 기록
        return null;
    }
}
