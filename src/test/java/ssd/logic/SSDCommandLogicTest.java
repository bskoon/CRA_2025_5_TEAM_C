package ssd.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ssd.IO.OutputIO;
import ssd.IO.SSDIO;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SSDCommandLogicTest {
    @Mock
    private SSDIO mockSSDIo;

    @Mock
    private OutputIO mockOutputIo;

    private SSDAppLogic ssdAppLogic;
    private SSDCommandLogic ssdCommandLogic;

    @BeforeEach
    void setUp() {
        ssdAppLogic = new SSDAppLogic(mockOutputIo, mockSSDIo);
        ssdCommandLogic = new SSDCommandLogic(ssdAppLogic, mockOutputIo);
    }

    @Test
    void Erase_삭제_사이즈가_정수가_아니면_ERROR_출력됨() {
        ssdCommandLogic.run(new String[]{"E", "0", "ee"});
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void Erase_삭제_사이즈가_음수이면_ERROR_출력됨() {
        ssdCommandLogic.run(new String[]{"E", "0", "-1"});
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void Erase_삭제_사이즈_10초과시_ERROR_출력됨() {
        ssdCommandLogic.run(new String[]{"E", "0", "11"});
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void Erase_LBA_범위_벗어나면_ERROR_출력됨() {
        ssdCommandLogic.run(new String[]{"E", "101", "10"});
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void Erase_LBA_음수이면_ERROR_출력됨() {
        ssdCommandLogic.run(new String[]{"E", "-1", "5"});
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void Erase_명령어_소문자일_경우_ERROR_출력됨() {
        ssdCommandLogic.run(new String[]{"e", "10", "5"});
        verify(mockOutputIo).write(0, "ERROR");
    }

    @Test
    void Erase_정상_명령어_수행시_해당_LBA_0으로_초기화됨() {
        ssdCommandLogic.run(new String[]{"E", "10", "5"});

        verify(mockSSDIo).write(10, "0x00000000");
        verify(mockSSDIo).write(11, "0x00000000");
        verify(mockSSDIo).write(12, "0x00000000");
        verify(mockSSDIo).write(13, "0x00000000");
        verify(mockSSDIo).write(14, "0x00000000");
    }
}