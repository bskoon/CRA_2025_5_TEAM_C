package shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class TestShell_exit_help_test {
/*
    private TestShell testShell;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    
    @BeforeEach
    void setUp() {
        // 테스트용 TestShell 인스턴스 생성
        testShell = new TestShell();
        
        // System.out 출력을 캡처하기 위한 설정
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
        // Arrange: TestShell이 실행 중인 상태
        assertTrue(testShell.isRunning());
        
        // Act: exit 함수 호출
        testShell.exit();
        
        // Assert: 
        // 1. 실행 상태가 false로 변경되어야 함
        assertFalse(testShell.isRunning());
        
        // 2. 종료 메시지가 출력되어야 함
        String output = outputStream.toString();
        assertTrue(output.contains("Exiting TestShell..."));
    }
   
    @Test
    void Help_팀명_포함_여부_확인() {
        // Act: help 함수 호출
        testShell.help();
        
        // Assert: 도움말 내용이 출력되어야 함
        String output = outputStream.toString();
        
        // 제작자 정보 확인
        assertTrue(output.contains("제작자: TEAM_C"));
    }
    
    @Test
    void Help_Write_설명_확인() {
        // Act: help 함수 호출
        testShell.help();
        
        // Assert: write 명령어 정보가 포함되어야 함
        String output = outputStream.toString();
        
        // write 명령어 존재 확인
        assertTrue(output.contains("• write"));
        
        // write 명령어 사용 예시 확인
        assertTrue(output.contains("ssd W 3 0x1298CDEF"));
        
        // write 명령어 설명 확인
        assertTrue(output.contains("3번 LBA 영역에 값 0x1298CDEF를 저장한다"));
    }
    
    @Test
    void Help_Read_설명_확인() {
        // Act: help 함수 호출
        testShell.help();
        
        // Assert: read 명령어 정보가 포함되어야 함
        String output = outputStream.toString();
        
        // read 명령어 존재 확인
        assertTrue(output.contains("• read"));
        
        // read 명령어 사용 예시 확인
        assertTrue(output.contains("ssd R 2"));
        
        // read 명령어 출력 결과 확인
        assertTrue(output.contains("출력결과: 0xAAAABBBB"));
    }

 */
}
