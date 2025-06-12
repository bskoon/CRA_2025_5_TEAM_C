package ssd.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ssd.IO.OutputIO;
import ssd.IO.SSDIO;
import ssd.buffer.CommandBuffer;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SSDCommandLogicTest {
    @Mock
    private SSDIO mockSSDIo;

    @Mock
    private OutputIO mockOutputIo;

    @Mock
    private CommandBuffer mockCommandBuffer;

    private SSDCommandLogic ssdCommandLogic;

    @BeforeEach
    void setUp() {
        ssdCommandLogic = new SSDCommandLogic(mockCommandBuffer);
    }

    @Test
    void Erase_삭제_사이즈가_정수가_아니면_ERROR_출력됨() {
        ssdCommandLogic.run(new String[]{"E", "0", "ee"});
        verify(mockCommandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void Erase_삭제_사이즈가_음수이면_ERROR_출력됨() {
        ssdCommandLogic.run(new String[]{"E", "0", "-1"});
        verify(mockCommandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void Erase_삭제_사이즈_10초과시_ERROR_출력됨() {
        ssdCommandLogic.run(new String[]{"E", "0", "11"});
        verify(mockCommandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void Erase_LBA_범위_벗어나면_ERROR_출력됨() {
        ssdCommandLogic.run(new String[]{"E", "101", "10"});
        verify(mockCommandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void Erase_LBA_음수이면_ERROR_출력됨() {
        ssdCommandLogic.run(new String[]{"E", "-1", "5"});
        verify(mockCommandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void Erase_명령어_소문자일_경우_ERROR_출력됨() {
        ssdCommandLogic.run(new String[]{"e", "10", "5"});
        verify(mockCommandBuffer).errorWrite(0, "ERROR");
    }

    @Test
    void Erase_정상_명령어_수행시_해당_LBA_0으로_초기화됨() {
        ssdCommandLogic.run(new String[]{"E", "10", "5"});

        verify(mockCommandBuffer).write(10, "0x00000000");
        verify(mockCommandBuffer).write(11, "0x00000000");
        verify(mockCommandBuffer).write(12, "0x00000000");
        verify(mockCommandBuffer).write(13, "0x00000000");
        verify(mockCommandBuffer).write(14, "0x00000000");
    }
}