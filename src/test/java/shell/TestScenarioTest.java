package shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shell.command.CommandType;
import shell.command.CommandLibrary;
import shell.command.ScenarioCommand;
import shell.scenario.*;
import shell.util.SSDCaller;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TestScenarioTest {
    @Mock
    SSDCaller ssdCaller;

    @Mock
    CommandLibrary mockCommandLibrary;

    ScenarioCommand scenarioCommand;
    Random random;
    TestScenario testScenario;
    ScenarioFactory scenarioFactory;

    @BeforeEach
    void setUp() {
        scenarioCommand = new ScenarioCommand(mockCommandLibrary);
        scenarioFactory = new ScenarioFactory(ssdCaller);
        random = new Random(1234);
    }

    @Test
    void FullWriteAndReadCompare_첫번째_시나리오테스트() throws IOException{
        // Arrange: readCompare가 "PASS"만 리턴하게 만들기
        testScenario = Mockito.spy(scenarioFactory.getScenario(CommandType.scenario1));
        doReturn("PASS").when(testScenario).readCompare(anyString(), anyString());

        // Act
        testScenario.executeScenario();

        // Assert
        verify(ssdCaller, times(100)).callSSD(eq("W"), anyString(), anyString());
        verify(testScenario, times(100)).readCompare(anyString(), anyString());
    }

    @Test
    void PartialLBAWrite_두번째_시나리오테스트() throws IOException{
        testScenario = Mockito.spy(scenarioFactory.getScenario(CommandType.scenario2));
        doReturn("PASS").when(testScenario).readCompare(anyString(), anyString());
        doNothing().when(ssdCaller).callSSD(eq("W"), anyString(), anyString());

        // Act
        testScenario.executeScenario();

        // Assert: check number of invocations
        verify(testScenario, times(150)).readCompare(anyString(), anyString());

        // Optional: check that specific LBA calls occurred (e.g., 30회씩)
        verify(ssdCaller, times(30)).callSSD(eq("W"), eq("0"), anyString());
        verify(ssdCaller, times(30)).callSSD(eq("W"), eq("1"), anyString());
        verify(ssdCaller, times(30)).callSSD(eq("W"), eq("2"), anyString());
        verify(ssdCaller, times(30)).callSSD(eq("W"), eq("3"), anyString());
        verify(ssdCaller, times(30)).callSSD(eq("W"), eq("4"), anyString());
    }

    @Test
    void WriteReadAging_세번째_시나리오테스트() throws IOException {
        // Arrange
        testScenario = Mockito.spy(scenarioFactory.getScenario(CommandType.scenario3));

        // 모든 readCompare는 PASS 리턴
        doReturn("PASS").when(testScenario).readCompare(anyString(), anyString());

        testScenario.executeScenario();

        // Assert
        verify(ssdCaller, times(200)).callSSD(eq("W"), eq("0"), anyString());
        verify(ssdCaller, times(200)).callSSD(eq("W"), eq("99"), anyString());

        verify(testScenario, times(200)).readCompare(eq("0"), anyString());
        verify(testScenario, times(200)).readCompare(eq("99"), anyString());
    }

    @Test
    void EraseAndWriteAging_네번째_시나리오테스트() throws IOException {
        testScenario = Mockito.spy(scenarioFactory.getScenario(CommandType.scenario4));

        // Arrange: 모든 readCompare가 "PASS" 반환
        doReturn("PASS").when(testScenario).readCompare(anyString(), eq("0x00000000"));

        testScenario.executeScenario();

        // Verify
        verify(ssdCaller, times(1)).callSSD(eq("E"), eq("0"), eq("3"));
        verify(testScenario, times(1)).readCompare(eq("0"), anyString()); // 초기 1번 + 30*49*1번
        verify(testScenario, times(1)).readCompare(eq("1"), anyString()); // 초기 1번 + 30*49*1번

        // 2부터 98까지 짝수 총 49개 LBA에 대해 각각 30번씩 erase
        for (int lba = 2; lba <= 98; lba += 2) {
            verify(ssdCaller, times(60)).callSSD(eq("W"), eq(Integer.toString(lba)), anyString());
            if (lba == 98)
                verify(ssdCaller, times(30)).callSSD(eq("E"), eq(Integer.toString(lba)), eq("2"));
            else
                verify(ssdCaller, times(30)).callSSD(eq("E"), eq(Integer.toString(lba)), eq("3"));

            if (lba == 2)
                verify(testScenario, times(31)).readCompare(eq(Integer.toString(lba)), anyString()); // 초기 1번 + 30
            else if (lba % 2 == 0)
                verify(testScenario, times(60)).readCompare(eq(Integer.toString(lba)), anyString()); // 초기 1번 + 30번
            else
                verify(testScenario, times(30)).readCompare(eq(Integer.toString(lba)), anyString()); // 초기 1번 + 30번

        }
        verify(testScenario, times(30)).readCompare(eq("99"), anyString()); // 초기 1번 + 30*49*1번
    }
}
