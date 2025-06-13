package ssd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.buffer.CommandBuffer;
import ssd.command.Command;
import ssd.logic.SSDCommandLogic;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SSDTest {
    @Mock
    private SSDIO mockSSDIo;

    @Mock
    private OutputIO mockOutputIo;

    private SSDCommandLogic ssdCommandLogic;

    @BeforeEach
    void setUp() {
        ssdCommandLogic = new SSDCommandLogic(mockSSDIo, mockOutputIo);
    }

    @Test
    void MainARGSValidCmd_소문자r_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"r", "10", "0xABCDEF12"});

        // then
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void MainARGSValidCmd_소문자w_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"w", "10", "0xABCDEF12"});

        // then
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void MainARGSValidCmd_소문자특수문자_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"#", "10", "0xABCDEF12"});

        // then
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void MainARGSValidCmd_소문자빈값_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"", "10", "0xABCDEF12"});

        // then
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void WriteValidLBA_마이너스_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"", "10", "0xABCDEF12"});

        // then
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void WriteValidLBA_100이상_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"W", "100", "0x12345678"});

        // then
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void WriteValidValue_Ox없을때_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"W", "10", "12345678"});

        // then
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void WriteValidValue_헥사벗어났을때_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"W", "10", "0xZZZZZZZZ"});

        // then
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void WriteValidValue_10자리아닐때_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"W", "10", "0x123"});

        // then
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void ReadValidLBA_마이너스_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"R", "-1"});

        // then
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void ReadValidLBA_100이상_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"R", "100"});

        // then
        verify(mockOutputIo).write(0, "ERROR");
    }
}