package shell;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void launchShell_1_과1_풀네임_테스트(){
        // 1. 원하는 입력값을 문자열로 준비
        String simulatedInput = "1_\n1_FullWriteAndReadCompare\nexit\n";
        // 2. ByteArrayInputStream을 이용해 System.in을 시뮬레이션
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));  // System.in을 가짜 입력으로 변경
        testShell.launchShell();

        assertTrue(outputStream.toString().contains("FAIL")||outputStream.toString().contains("PASS"));
    }
    @Test
    void launchShell_2_과2_풀네임_테스트(){
        // 1. 원하는 입력값을 문자열로 준비
        String simulatedInput = "2_\n2_PartialLBAWrite\nexit\n";
        // 2. ByteArrayInputStream을 이용해 System.in을 시뮬레이션
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));  // System.in을 가짜 입력으로 변경
        testShell.launchShell();

        assertTrue(outputStream.toString().contains("FAIL")||outputStream.toString().contains("PASS"));

    }
    @Test
    void launchShell_3_과3_풀네임_테스트(){
        // 1. 원하는 입력값을 문자열로 준비
        String simulatedInput = "3_\n3_WriteReadAging\nexit\n";
        // 2. ByteArrayInputStream을 이용해 System.in을 시뮬레이션
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));  // System.in을 가짜 입력으로 변경
        testShell.launchShell();

        assertTrue(outputStream.toString().contains("FAIL")||outputStream.toString().contains("PASS"));

    }
}
