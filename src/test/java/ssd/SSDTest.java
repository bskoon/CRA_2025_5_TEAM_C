package ssd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SSDTest {
    private IOHandler mockIO;
    private VirtualSSD ssd;
    private SSDAppLogic logic;

    @BeforeEach
    void setUp() {
        mockIO = mock(IOHandler.class);      // IOHandler는 read/write 인터페이스
        ssd = new VirtualSSD(mockIO);              // 실제 SSD 시뮬레이터
        logic = new SSDAppLogic(ssd, mockIO); // 테스트 대상
    }

    @Test
    void MainARGSValidCmd_소문자r_예외처리() {
        // when
        logic.run(new String[]{"r", "10", "0xABCDEF12"});

        // then
        verify(mockIO).outPutWrite("ERROR");
    }

    @Test
    void MainARGSValidCmd_소문자w_예외처리() {
        // when
        logic.run(new String[]{"w", "10", "0xABCDEF12"});

        // then
        verify(mockIO).outPutWrite("ERROR");
    }

    @Test
    void MainARGSValidCmd_소문자특수문자_예외처리() {
        // when
        logic.run(new String[]{"#", "10", "0xABCDEF12"});

        // then
        verify(mockIO).outPutWrite("ERROR");
    }

    @Test
    void MainARGSValidCmd_소문자빈값_예외처리() {
        // when
        logic.run(new String[]{"", "10", "0xABCDEF12"});

        // then
        verify(mockIO).outPutWrite("ERROR");
    }

    @Test
    void WriteValidLBA_마이너스_예외처리() {
        // when
        logic.run(new String[]{"", "10", "0xABCDEF12"});

        // then
        verify(mockIO).outPutWrite("ERROR");
    }

    @Test
    void WriteValidLBA_100이상_예외처리() {
        // when
        ssd.write(100, "0x12345678");

        // then
        verify(mockIO).outPutWrite("ERROR");
    }

    @Test
    void WriteValidValue_Ox없을때_예외처리() {
        // when
        ssd.write(10, "12345678");

        // then
        verify(mockIO).outPutWrite("ERROR");
    }

    @Test
    void WriteValidValue_헥사벗어났을때_예외처리() {
        // when
        ssd.write(10, "0xZZZZZZZZ");

        // then
        verify(mockIO).outPutWrite("ERROR");
    }

    @Test
    void WriteValidValue_10자리아닐때_예외처리() {
        // when
        ssd.write(10, "0x123");

        // then
        verify(mockIO).outPutWrite("ERROR");
    }

    @Test
    void Write_테스트() {
        // when
        ssd.write(10, "0xABCDEF01");

        // then
        verify(mockIO).nandWrite("0xABCDEF01");
    }

    @Test
    void ReadValidLBA_마이너스_예외처리() {
        // when
        ssd.read(-1);

        // then
        verify(mockIO).outPutWrite("ERROR");
    }

    @Test
    void ReadValidLBA_100이상_예외처리() {
        // when
        ssd.read(100);

        // then
        verify(mockIO).outPutWrite("ERROR");
    }

    @Test
    void Read_테스트() {
        // when
        ssd.write(5, "0xABCDEF01");
        ssd.read(5);

        // then
        verify(mockIO).outPutWrite("0xABCDEF01");
    }

    @Test
    void Read_Write_설정하지_않은값() {
        // when
        ssd.read(5);

        // then
        verify(mockIO).outPutWrite("0x00000000");
    }
}