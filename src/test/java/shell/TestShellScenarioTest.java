package shell;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class TestShellScenarioTest {

    private TestShell testShell;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private InputStream originIn;

    @BeforeEach
    void setUp() {
        // 테스트용 TestShell 인스턴스 생성
        testShell = new TestShell();

        // System.out 출력을 캡처하기 위한 설정
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        originIn = System.in;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // System.out 복원
        System.setOut(originalOut);
        System.setIn(originIn);
    }

    @Test
    void launchShell_1_테스트(){
        TestShell spyShell = spy(TestShell.class);
        // 1. 원하는 입력값을 문자열로 준비
        String simulatedInput = "1_\nexit\n";
        // 2. ByteArrayInputStream을 이용해 System.in을 시뮬레이션
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));  // System.in을 가짜 입력으로 변경
        doReturn("0xFFFFFFFF").when(spyShell).readLBA(anyInt());

        spyShell.launchShell();

        assertTrue(outputStream.toString().contains("FAIL")||outputStream.toString().contains("PASS"));
    }
    @Test
    void launchShell_1_풀네임_테스트(){
        TestShell spyShell = spy(TestShell.class);
        // 1. 원하는 입력값을 문자열로 준비
        String simulatedInput = "1_FullWriteAndReadCompare\nexit\n";
        // 2. ByteArrayInputStream을 이용해 System.in을 시뮬레이션
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));  // System.in을 가짜 입력으로 변경
        doReturn("0xFFFFFFFF").when(spyShell).readLBA(anyInt());

        spyShell.launchShell();

        assertTrue(outputStream.toString().contains("FAIL")||outputStream.toString().contains("PASS"));
    }
    @Test
    void launchShell_2_테스트(){
        TestShell spyShell = spy(TestShell.class);
        // 1. 원하는 입력값을 문자열로 준비
        String simulatedInput = "2_\nexit\n";
        // 2. ByteArrayInputStream을 이용해 System.in을 시뮬레이션
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));  // System.in을 가짜 입력으로 변경
        doReturn("0xFFFFFFFF").when(spyShell).readLBA(anyInt());

        spyShell.launchShell();

        assertTrue(outputStream.toString().contains("FAIL")||outputStream.toString().contains("PASS"));

    }
    @Test
    void launchShell_2_풀네임_테스트(){
        TestShell spyShell = spy(TestShell.class);
        // 1. 원하는 입력값을 문자열로 준비
        String simulatedInput = "2_PartialLBAWrite\nexit\n";
        // 2. ByteArrayInputStream을 이용해 System.in을 시뮬레이션
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));  // System.in을 가짜 입력으로 변경
        doReturn("0xFFFFFFFF").when(spyShell).readLBA(anyInt());

        spyShell.launchShell();

        assertTrue(outputStream.toString().contains("FAIL")||outputStream.toString().contains("PASS"));

    }
    @Test
    void launchShell_3_테스트(){
        TestShell spyShell = spy(TestShell.class);
        // 1. 원하는 입력값을 문자열로 준비
        String simulatedInput = "3_\nexit\n";
        // 2. ByteArrayInputStream을 이용해 System.in을 시뮬레이션
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));  // System.in을 가짜 입력으로 변경
        doReturn("0xFFFFFFFF").when(spyShell).readLBA(anyInt());

        spyShell.launchShell();

        assertTrue(outputStream.toString().contains("FAIL")||outputStream.toString().contains("PASS"));

    }
    @Test
    void launchShell_3_풀네임_테스트(){
        TestShell spyShell = spy(TestShell.class);
        // 1. 원하는 입력값을 문자열로 준비
        String simulatedInput = "3_WriteReadAging\nexit\n";
        // 2. ByteArrayInputStream을 이용해 System.in을 시뮬레이션
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));  // System.in을 가짜 입력으로 변경

        doReturn("0xFFFFFFFF").when(spyShell).readLBA(anyInt());

        spyShell.launchShell();

        assertTrue(outputStream.toString().contains("FAIL")||outputStream.toString().contains("PASS"));

    }
}
