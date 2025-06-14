package shell;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import shell.util.Utility;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TestShell_exit_help_test {
    private final Utility util = Utility.getInstance();
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private TestShell testShell;

    @BeforeEach
    void setUp() {
        // 테스트용 TestShell_exit_help_test 인스턴스 생성
        testShell = new TestShell(util.getCommandExecutor());
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        // System.out 복원
        System.setOut(originalOut);
    }

    @Test
    void Exit_동작_여부_확인() {
        // Arrange
        assertTrue(testShell.isRunning());

        // Act: private exit() 호출
        try {
            java.lang.reflect.Method exitMethod = TestShell.class.getDeclaredMethod("exit");
            exitMethod.setAccessible(true);  // private 접근 허용
            exitMethod.invoke(testShell);
        } catch (Exception e) {
            fail();
        }

        // Assert
        assertFalse(testShell.isRunning());

        String output = outputStream.toString();
        assertTrue(output.contains("Exiting TestShell..."));
    }

    private void changePrivateToPublicMethod(PrintStream originalOut) {
        try {
            TestShell testShell = new TestShell(util.getCommandExecutor());
            java.lang.reflect.Method helpMethod = TestShell.class.getDeclaredMethod("help");
            helpMethod.setAccessible(true);
            helpMethod.invoke(testShell);
        }  catch (Exception e) {
            fail();
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void Help_팀명_포함_여부_확인() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        changePrivateToPublicMethod(originalOut);

        String output = outputStream.toString();
        assertTrue(output.contains("제작자: TEAM_C"));
    }

    @Test
    void Help_Write_설명_확인() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        changePrivateToPublicMethod(originalOut);

        String output = outputStream.toString();

        // write 명령어 사용 예시 확인
        assertTrue(output.contains("write 3 0x1298CDEF"));
    }



    @Test
    void Help_Read_설명_확인() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        changePrivateToPublicMethod(originalOut);

        String output = outputStream.toString();

        // read 명령어 사용 예시 확인
        assertTrue(output.contains("read 2"));
    }

    @Test
    void Help_fullwrite_설명_확인() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        changePrivateToPublicMethod(originalOut);

        String output = outputStream.toString();

        // fullwrite 명령어 존재 확인
        assertTrue(output.contains("• fullwrite"));

        // fullwrite 명령어 사용 예시 확인
        assertTrue(output.contains("fullwrite 0xABCDFFFF"));
    }

    @Test
    void Help_fullread_설명_확인() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        changePrivateToPublicMethod(originalOut);

        String output = outputStream.toString();

        // fullread 명령어 존재 확인
        assertTrue(output.contains("• fullread"));

        // fullread 명령어 사용 예시 확인
        assertTrue(output.contains("fullread"));
    }
}
