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

    @Mock
    private CommandBuffer commandBuffer;

    private SSDCommandLogic ssdCommandLogic;

    @BeforeEach
    void setUp() {
        ssdCommandLogic = new SSDCommandLogic(commandBuffer);
    }

    @Test
    void MainARGSValidCmd_소문자r_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"r", "10", "0xABCDEF12"});

        // then
        verify(commandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void MainARGSValidCmd_소문자w_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"w", "10", "0xABCDEF12"});

        // then
        verify(commandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void MainARGSValidCmd_소문자특수문자_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"#", "10", "0xABCDEF12"});

        // then
        verify(commandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void MainARGSValidCmd_소문자빈값_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"", "10", "0xABCDEF12"});

        // then
        verify(commandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void WriteValidLBA_마이너스_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"", "10", "0xABCDEF12"});

        // then
        verify(commandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void WriteValidLBA_100이상_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"W", "100", "0x12345678"});

        // then
        verify(commandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void WriteValidValue_Ox없을때_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"W", "10", "12345678"});

        // then
        verify(commandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void WriteValidValue_헥사벗어났을때_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"W", "10", "0xZZZZZZZZ"});

        // then
        verify(commandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void WriteValidValue_10자리아닐때_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"W", "10", "0x123"});

        // then
        verify(commandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void Write_테스트() {
        // when
        ssdCommandLogic.run(new String[]{"W", "10", "0xABCDEF01"});

        // then
        verify(commandBuffer).write(10, "0xABCDEF01");
    }

    @Test
    void ReadValidLBA_마이너스_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"R", "-1"});

        // then
        verify(commandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void ReadValidLBA_100이상_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"R", "100"});

        // then
        verify(commandBuffer).errorWrite(0, "ERROR");
    }

    @Disabled
    @Test
    void Read_테스트() {
        // when
        when(mockSSDIo.read(5)).thenReturn("0xABCDEF01");
        ssdCommandLogic.run(new String[]{"W", "5", "0xABCDEF01"});
        ssdCommandLogic.run(new String[]{"R", "5"});

        // then
        verify(mockSSDIo).write(5, "0xABCDEF01");
        verify(mockSSDIo).read(5);
        verify(mockOutputIo).write(0, "0xABCDEF01");
    }

    @Disabled
    @Test
    void Read_Write_설정하지_않은값() {
        // when
        when(mockSSDIo.read(5)).thenReturn("0x00000000");
        ssdCommandLogic.run(new String[]{"R", "5"});

        // then
        verify(mockOutputIo).write(0, "0x00000000");
    }
}