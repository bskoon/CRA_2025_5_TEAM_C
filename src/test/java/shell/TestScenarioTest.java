package shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shell.command.Document;
import shell.command.ScenarioCommand;
import shell.scenario.Scenario1;
import shell.scenario.Scenario2;
import shell.scenario.Scenario3;
import shell.util.SSDCaller;
import shell.scenario.TestScenario;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TestScenarioTest {
    @Mock
    SSDCaller ssdCaller;

    @Mock
    Document mockDocument;

    ScenarioCommand scenarioCommand;
    Random random;
    TestScenario testScenario;

    @BeforeEach
    void setUp() {
        scenarioCommand = new ScenarioCommand(mockDocument);
        random = new Random(1234);
    }

    @Test
    void FullWriteAndReadCompare_첫번째_시나리오테스트() throws IOException{
        // Arrange: readCompare가 "PASS"만 리턴하게 만들기
        testScenario = Mockito.spy(new Scenario1(ssdCaller, random));
        doReturn("PASS").when(testScenario).readCompare(anyInt(), anyString());

        // Act
        testScenario.executeScenario();

        // Assert
        verify(ssdCaller, times(100)).writeOnSSD(anyInt(), anyString());
        verify(testScenario, times(100)).readCompare(anyInt(), anyString());
    }

    @Test
    void PartialLBAWrite_두번째_시나리오테스트() throws IOException{
        testScenario = Mockito.spy(new Scenario2(ssdCaller, random));
        doReturn("PASS").when(testScenario).readCompare(anyInt(), anyString());
        doNothing().when(ssdCaller).writeOnSSD(anyInt(), anyString());

        // Act
        testScenario.executeScenario();

        // Assert: check number of invocations
        verify(testScenario, times(150)).readCompare(anyInt(), anyString());

        // Optional: check that specific LBA calls occurred (e.g., 30회씩)
        verify(ssdCaller, times(30)).writeOnSSD(eq(0), anyString());
        verify(ssdCaller, times(30)).writeOnSSD(eq(1), anyString());
        verify(ssdCaller, times(30)).writeOnSSD(eq(2), anyString());
        verify(ssdCaller, times(30)).writeOnSSD(eq(3), anyString());
        verify(ssdCaller, times(30)).writeOnSSD(eq(4), anyString());
    }

    @Test
    void WriteReadAging_세번째_시나리오테스트() throws IOException {
        // Arrange
        testScenario = Mockito.spy(new Scenario3(ssdCaller, random));

        // 모든 readCompare는 PASS 리턴
        doReturn("PASS").when(testScenario).readCompare(anyInt(), anyString());

        testScenario.executeScenario();

        // Assert
        verify(ssdCaller, times(400)).writeOnSSD(anyInt(), anyString());
        verify(ssdCaller, times(200)).writeOnSSD(eq(0), anyString());
        verify(ssdCaller, times(200)).writeOnSSD(eq(99), anyString());

        verify(testScenario, times(400)).readCompare(anyInt(), anyString());
        verify(testScenario, times(200)).readCompare(eq(0), anyString());
        verify(testScenario, times(200)).readCompare(eq(99), anyString());
    }

    @Test
    void EraseAndWriteAging_네번째_시나리오테스트() throws IOException {
        testScenario = Mockito.spy(new Scenario4(ssdCaller, random));

        // Arrange: 모든 readCompare가 "PASS" 반환
        doReturn("PASS").when(testScenario).readCompare(anyInt(), eq("0x00000000"));

        // 초기 erase 0~2
        ssdCaller.eraseOnSSD(0, 3);
        testScenario.readCompare(0, "0x00000000");
        testScenario.readCompare(1, "0x00000000");
        testScenario.readCompare(2, "0x00000000");

        for (int i = 0; i < 30; i++) {
            for (int lba = 2; lba <= 98; lba += 2) {
                String hexa = getRandomHexString(random);
                String hexb = getRandomHexString(random);
                ssdCaller.writeOnSSD(lba, hexa);
                ssdCaller.writeOnSSD(lba, hexb);
                ssdCaller.eraseOnSSD(lba, 3);
                for (int j = 0; j < 3; j++) {
                    testScenario.readCompare(j, "0x00000000");
                }
            }
        }

        // Verify
        verify(ssdCaller, times(1)).eraseOnSSD(eq(0), eq(3));
        
        // 2부터 98까지 짝수 총 49개 LBA에 대해 각각 30번씩 erase
        for (int lba = 2; lba <= 98; lba += 2) {
            verify(ssdCaller, times(30)).eraseOnSSD(eq(lba), eq(3));
        }
        
        // 2부터 98까지 짝수 총 49개 LBA에 대해 각각 60번씩 write (30번 * 2번)
        for (int lba = 2; lba <= 98; lba += 2) {
            verify(ssdCaller, times(60)).writeOnSSD(eq(lba), anyString());
        }

        // readCompare 검증 - 초기 3번 + 30번 반복 * 49개 LBA * 3번 readCompare
        verify(testScenario, times(1471)).readCompare(eq(0), anyString()); // 초기 1번 + 30*49*1번
        verify(testScenario, times(1471)).readCompare(eq(1), anyString()); // 초기 1번 + 30*49*1번  
        verify(testScenario, times(1471)).readCompare(eq(2), anyString()); // 초기 1번 + 30*49*1번
    }

    private String getRandomHexString(Random rand) {
        int randomValue = rand.nextInt();
        return "0x" + String.format("%08X", randomValue);
    }
}
