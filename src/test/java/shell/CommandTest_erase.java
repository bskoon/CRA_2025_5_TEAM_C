package shell;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;
import shell.command.Document;
import shell.command.EraseCommand;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CommandTest_erase {
    private EraseCommand eraseCommand;
    private Document mockDocument;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setup() {
        // Arrange
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        mockDocument = mock(Document.class);
        eraseCommand = new EraseCommand(mockDocument);
    }

    @AfterEach
    void tearDown() {
        // System.out 복원
        System.setOut(originalOut);
    }

    @Test
    void Erase_size_미변경_케이스_테스트(){
        // Given
        String[] args = {"erase", "98", "1"};

        // When
        eraseCommand.execute(args);

        // Then
        verify(mockDocument).erase(98, 1);
    }

    @Test
    void Erase_음수_size_미변경_케이스_테스트(){
        // Given
        String[] args = {"erase", "98", "-1"};

        // When
        eraseCommand.execute(args);

        // Then
        verify(mockDocument).erase(98, 1);
    }

    @Test
    void Erase_size_변경_케이스_테스트(){
        String[] args = {"erase", "98", "3"};

        // When
        eraseCommand.execute(args);

        // Then
        verify(mockDocument).erase(98, 2);
    }

    @Test
    void Erase_음수_size_변경_케이스_테스트(){
        String[] args = {"erase", "98", "-3"};

        // When
        eraseCommand.execute(args);

        // Then
        verify(mockDocument).erase(96, 3);
    }

    @Test
    void Erase_음수_변경_케이스_테스트(){
        String[] args = {"erase", "1", "-3"};

        // When
        eraseCommand.execute(args);

        // Then
        verify(mockDocument).erase(0, 2);
    }

    @Test
    void Erase_Range_size_미변경_케이스_테스트(){
        // Given
        String[] args = {"erase_range", "98", "99"}; // startLBA = 98, endLBA = 99 → size = 2

        // When
        eraseCommand.execute(args);

        // Then
        verify(mockDocument).erase(98, 2);
    }

    @Test
    void Erase_최대_사이즈_초과시_나눠서_보내는지_테스트(){
        // Given
        String[] args = {"erase", "80", "15"};

        // When
        eraseCommand.execute(args);

        // Then
        InOrder inOrder = inOrder(mockDocument);
        inOrder.verify(mockDocument).erase(80, 10); // 첫 chunk
        inOrder.verify(mockDocument).erase(90, 5);  // 두 번째 chunk

        // 정확히 두 번만 호출되었는지 확인 (불필요한 호출 방지)
        verify(mockDocument, times(2)).erase(anyInt(), anyInt());
    }

    @Test
    void Erase_size_음수일때_최대_사이즈_초과시_나눠서_보내는지_테스트(){
        // Given
        String[] args = {"erase", "80", "-15"};

        // When
        eraseCommand.execute(args);

        // Then
        InOrder inOrder = inOrder(mockDocument);
        inOrder.verify(mockDocument).erase(66, 10); // 첫 chunk
        inOrder.verify(mockDocument).erase(76, 5);  // 두 번째 chunk

        // 정확히 두 번만 호출되었는지 확인 (불필요한 호출 방지)
        verify(mockDocument, times(2)).erase(anyInt(), anyInt());
    }

    @Test
    void Erase_Range_최대_사이즈_초과시_나눠서_보내는지_테스트(){
        // Given
        String[] args = {"erase_range", "80", "95"}; // size = 16

        // When
        eraseCommand.execute(args);

        // Then
        InOrder inOrder = inOrder(mockDocument);
        inOrder.verify(mockDocument).erase(80, 10); // 첫 chunk
        inOrder.verify(mockDocument).erase(90, 6);  // 두 번째 chunk

        // 정확히 두 번만 호출되었는지 검증
        verify(mockDocument, times(2)).erase(anyInt(), anyInt());
    }

    @Test
    void Erase_Range_반대_순서_최대_사이즈_초과시_나눠서_보내는지_테스트(){
        // Given
        String[] args = {"erase_range", "95", "80"}; // size = 16

        // When
        eraseCommand.execute(args);

        // Then
        InOrder inOrder = inOrder(mockDocument);
        inOrder.verify(mockDocument).erase(80, 10); // 첫 chunk
        inOrder.verify(mockDocument).erase(90, 6);  // 두 번째 chunk

        // 정확히 두 번만 호출되었는지 검증
        verify(mockDocument, times(2)).erase(anyInt(), anyInt());
    }

    @Test
    void Erase_lba_범위_벗어나는_케이스_테스트(){
        // Given
        String[] args = {"erase", "100", "3"}; // LBA = 100 → 범위 초과

        // When
        eraseCommand.execute(args);

        // Then
        String output = outputStream.toString().trim();
        assertTrue(output.contains("INVALID COMMAND"));
        verify(mockDocument, never()).erase(anyInt(), anyInt()); // 실제 호출 없음
    }


    @Test
    void Erase_range_lba_벗어나는_케이스_테스트(){
        // Given
        String[] args = {"erase_range", "80", "100"}; // endLBA = 100 → 유효 범위 초과

        // When
        eraseCommand.execute(args);

        // Then
        String output = outputStream.toString().trim();
        assertTrue(output.contains("INVALID COMMAND"));
        verify(mockDocument, never()).erase(anyInt(), anyInt()); // 문서 조작은 없어야 함
    }
}
