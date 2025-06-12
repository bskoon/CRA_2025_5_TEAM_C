package ssd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.logic.SSDAppLogic;
import ssd.logic.SSDCommandLogic;

import static org.mockito.Mockito.*;

class SSDTest {
    private SSDIO mockSSDIo;
    private OutputIO mockOutputIo;
    private SSDAppLogic ssdAppLogic;
    private SSDCommandLogic ssdCommandLogic;

    @BeforeEach
    void setUp() {
        mockSSDIo = mock(SSDIO.class);
        mockOutputIo = mock(OutputIO.class);
        ssdAppLogic = new SSDAppLogic(mockOutputIo, mockSSDIo);
        ssdCommandLogic = new SSDCommandLogic(ssdAppLogic, mockOutputIo,mockSSDIo);
    }

    @Test
    void MainARGSValidCmd_소문자r_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"r", "10", "0xABCDEF12"});

        // then
        verify(mockOutputIo).write(0,"ERROR");
    }

    @Test
    void MainARGSValidCmd_소문자w_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"w", "10", "0xABCDEF12"});

        // then
        verify(mockOutputIo).write(0,"ERROR");
    }

    @Test
    void MainARGSValidCmd_소문자특수문자_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"#", "10", "0xABCDEF12"});

        // then
        verify(mockOutputIo).write(0,"ERROR");
    }

    @Test
    void MainARGSValidCmd_소문자빈값_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"", "10", "0xABCDEF12"});

        // then
        verify(mockOutputIo).write(0,"ERROR");
    }

    @Test
    void WriteValidLBA_마이너스_예외처리() {
        // when
        ssdCommandLogic.run(new String[]{"", "10", "0xABCDEF12"});

        // then
        verify(mockOutputIo).write(0,"ERROR");
    }

    @Test
    void WriteValidLBA_100이상_예외처리() {
        // when
        ssdAppLogic.write(100, "0x12345678");

        // then
        verify(mockOutputIo).write(0,"ERROR");
    }

    @Test
    void WriteValidValue_Ox없을때_예외처리() {
        // when
        ssdAppLogic.write(10, "12345678");

        // then
        verify(mockOutputIo).write(0,"ERROR");
    }

    @Test
    void WriteValidValue_헥사벗어났을때_예외처리() {
        // when
        ssdAppLogic.write(10, "0xZZZZZZZZ");

        // then
        verify(mockOutputIo).write(0,"ERROR");
    }

    @Test
    void WriteValidValue_10자리아닐때_예외처리() {
        // when
        ssdAppLogic.write(10, "0x123");

        // then
        verify(mockOutputIo).write(0,"ERROR");
    }

    @Test
    void Write_테스트() {
        // when
        ssdAppLogic.write(10, "0xABCDEF01");

        // then
        verify(mockSSDIo).write(10,"0xABCDEF01");
    }

    @Test
    void ReadValidLBA_마이너스_예외처리() {
        // when
        ssdAppLogic.read(-1);

        // then
        verify(mockOutputIo).write(0,"ERROR");
    }

    @Test
    void ReadValidLBA_100이상_예외처리() {
        // when
        ssdAppLogic.read(100);

        // then
        verify(mockOutputIo).write(0,"ERROR");
    }

    @Test
    void Read_테스트() {
        // when
        when(mockSSDIo.read(5)).thenReturn("0xABCDEF01");
        ssdAppLogic.write(5, "0xABCDEF01");
        ssdAppLogic.read(5);

        // then
        verify(mockSSDIo).write(5,"0xABCDEF01");
        verify(mockSSDIo).read(5);
        verify(mockOutputIo).write(0,"0xABCDEF01");
    }

    @Test
    void Read_Write_설정하지_않은값() {
        // when
        when(mockSSDIo.read(5)).thenReturn("0x00000000");
        ssdAppLogic.read(5);

        // then
        verify(mockOutputIo).write(0,"0x00000000");
    }
}