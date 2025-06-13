package ssd.buffer;

import ssd.SSDConstant;
import ssd.common.ValidCheck;

public class SSDArgument {
    private int lba;
    private int size;
    private String value;
    private String command;
    private final String[] args;

    public SSDArgument(String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("명령어 인자가 비어있습니다.");
        }

        this.args = args;
        this.command = args[0];

        ValidCheck.validateCommandType(command);

        switch (command) {
            case "R" -> handleRead();
            case "W" -> handleWrite();
            case "E" -> handleErase();
            default -> throw new IllegalArgumentException("알 수 없는 명령어: " + command);
        }
    }

    private void handleRead() {
        validateLength(2);
        validateLba(args[1]);
        this.lba = Integer.parseInt(args[1]);
    }

    private void handleWrite() {
        validateLength(3);
        validateLba(args[1]);
        if (!ValidCheck.isValidHex32(args[2])) {
            throw new IllegalArgumentException("32비트 16진수 값이 아닙니다: " + args[2]);
        }
        this.lba = Integer.parseInt(args[1]);
        this.value = args[2];
    }

    private void handleErase() {
        validateLength(3);
        validateLba(args[1]);
        if (!ValidCheck.isStringBetween0And10(args[2])) {
            throw new IllegalArgumentException("0~10 사이 숫자여야 합니다: " + args[2]);
        }

        this.lba = Integer.parseInt(args[1]);
        this.size = Integer.parseInt(args[2]);

        if (lba + size > SSDConstant.MAX_LBA) {
            throw new IllegalArgumentException("LBA + Size가 최대 허용 범위를 초과합니다.");
        }
    }

    private void validateLength(int expectedLength) {
        if (args.length != expectedLength) {
            throw new IllegalArgumentException("명령어 인자 개수가 잘못되었습니다. 기대값: " + expectedLength);
        }
    }

    private void validateLba(String lbaStr) {
        if (!ValidCheck.isInRange0to99(lbaStr)) {
            throw new IllegalArgumentException("LBA가 0~99 범위를 벗어났습니다: " + lbaStr);
        }
    }

    public int getLba() {
        return lba;
    }

    public int getSize() {
        return size;
    }

    public String getValue() {
        return value;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }
}
