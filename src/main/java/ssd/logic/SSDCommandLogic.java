package ssd.logic;

import ssd.IO.IOHandler;

public class SSDCommandLogic {
    private final SSDAppLogic ssdAppLogic;
    private final IOHandler ioHandler;

    public SSDCommandLogic(SSDAppLogic ssdAppLogic, IOHandler ioHandler) {
        this.ssdAppLogic = ssdAppLogic;
        this.ioHandler = ioHandler;
    }

    public void run(String[] args) {
        // todo :: main 로직 여기로 이동
        // todo :: rw 만 판단 다음 로직은 SsdAppLogic 처리
    }
}
