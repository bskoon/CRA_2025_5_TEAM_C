package shell;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class TestShell_erase {
    static EraseCommand eraseCommand;

    @BeforeAll
    static void beforeAll() {
        // Arrange
        eraseCommand = Mockito.spy(new EraseCommand());
    }

    @Test
    void Erase_size_미변경_케이스_테스트(){
        int lba = 98;
        int size = 1;
        int realsize = 1;

        // preprocess 결과를 stub
        doReturn(realsize).when(eraseCommand).calErasedBufferSize(lba, size);

        // Act
        eraseCommand.callSsdEraseProcess(lba, size);

        // Assert
        verify(eraseCommand).generateCommand("E", lba, String.valueOf(realsize));
    }

    @Test
    void Erase_size_변경_케이스_테스트(){
        int lba = 98;
        int size = 3;
        int realsize = 2;

        // preprocess 결과를 stub
        doReturn(realsize).when(eraseCommand).calErasedBufferSize(lba, size);

        // Act
        eraseCommand.callSsdEraseProcess(lba, size);

        // Assert
        verify(eraseCommand).generateCommand("E", lba, String.valueOf(realsize));
    }

    @Test
    void Erase_Range_size_미변경_케이스_테스트(){
        int startlba = 98;
        int endlba = 99;
        int realsize = 2;

        // preprocess 결과를 stub
        doReturn(realsize).when(eraseCommand).calErasedRangedBufferSize(startlba, endlba);

        // Act
        eraseCommand.callSsdEraseRangeProcess(startlba, endlba);

        // Assert
        verify(eraseCommand).generateCommand("E", startlba, String.valueOf(realsize));
    }

    @Test
    void Erase_Range_size_변경_케이스_테스트(){
        int startlba = 98;
        int endlba = 101;
        int realsize = 2;

        // preprocess 결과를 stub
        doReturn(realsize).when(eraseCommand).calErasedRangedBufferSize(startlba, endlba);

        // Act
        eraseCommand.callSsdEraseRangeProcess(startlba, endlba);

        // Assert
        verify(eraseCommand).generateCommand("E", startlba, String.valueOf(realsize));
    }

    @Test
    void Erase_최대_사이즈_초과시_나눠서_보내는지_테스트(){
        int lba = 80;
        int size = 15;
        int nextlba = 90;
        int nextsize = 5;
        int returnSize = 10;
        int nextReturnSize = 5;

        // 각 청크 사이즈에 대해 calErasedBufferSize stub
        doReturn(returnSize).when(eraseCommand).calErasedBufferSize(lba, size);
        doReturn(nextReturnSize).when(eraseCommand).calErasedBufferSize(nextlba, nextsize);

        // Act
        eraseCommand.callSsdEraseProcess(lba, size);

        // Assert
        // 각각의 chunk에 대해 generateCommand가 호출되었는지 검증
        verify(eraseCommand).generateCommand("E", 80, String.valueOf(returnSize));  // 첫 청크
        verify(eraseCommand).generateCommand("E", 90, String.valueOf(nextReturnSize));  // 두 번째 청크
    }

    @Test
    void Erase_Range_최대_사이즈_초과시_나눠서_보내는지_테스트(){
        int startlba = 80;
        int endlba = 95;
        int nextlba = 90;
        int returnSize = 10;
        int nextReturnSize = 5;

        // preprocess 결과를 stub
        doReturn(returnSize).when(eraseCommand).calErasedRangedBufferSize(startlba, endlba);
        doReturn(nextReturnSize).when(eraseCommand).calErasedRangedBufferSize(startlba, nextlba);

        // Act
        eraseCommand.callSsdEraseRangeProcess(startlba, endlba);

        // Assert
        verify(eraseCommand).generateCommand("E", startlba, String.valueOf(returnSize));
        verify(eraseCommand).generateCommand("E", nextlba, String.valueOf(nextReturnSize));  // 두 번째 청크
    }
}
