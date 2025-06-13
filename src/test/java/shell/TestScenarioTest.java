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
            String hex2a = getRandomHexString(random);
            String hex2b = getRandomHexString(random);
            ssdCaller.writeOnSSD(2, hex2a);
            ssdCaller.writeOnSSD(2, hex2b);
            ssdCaller.eraseOnSSD(2, 3);
            testScenario.readCompare(2, "0x00000000");
            testScenario.readCompare(3, "0x00000000");
            testScenario.readCompare(4, "0x00000000");

            String hex4a = getRandomHexString(random);
            String hex4b = getRandomHexString(random);
            ssdCaller.writeOnSSD(4, hex4a);
            ssdCaller.writeOnSSD(4, hex4b);
            ssdCaller.eraseOnSSD(4, 3);
            testScenario.readCompare(4, "0x00000000");
            testScenario.readCompare(5, "0x00000000");
            testScenario.readCompare(6, "0x00000000");

            String hex6a = getRandomHexString(random);
            String hex6b = getRandomHexString(random);
            ssdCaller.writeOnSSD(6, hex6a);
            ssdCaller.writeOnSSD(6, hex6b);
            ssdCaller.eraseOnSSD(6, 3);
            testScenario.readCompare(6, "0x00000000");
            testScenario.readCompare(7, "0x00000000");
            testScenario.readCompare(8, "0x00000000");
        }

        // Verify
        verify(ssdCaller, times(1)).eraseOnSSD(eq(0), eq(3));
        verify(ssdCaller, times(30)).eraseOnSSD(eq(2), eq(3));
        verify(ssdCaller, times(30)).eraseOnSSD(eq(4), eq(3));
        verify(ssdCaller, times(30)).eraseOnSSD(eq(6), eq(3));

        verify(ssdCaller, times(60)).writeOnSSD(eq(2), anyString());
        verify(ssdCaller, times(60)).writeOnSSD(eq(4), anyString());
        verify(ssdCaller, times(60)).writeOnSSD(eq(6), anyString());

        verify(testScenario, times(1)).readCompare(eq(0), anyString());
        verify(testScenario, times(1)).readCompare(eq(1), anyString());
        verify(testScenario, times(31)).readCompare(eq(2), anyString());
        verify(testScenario, times(30)).readCompare(eq(3), anyString());
        verify(testScenario, times(60)).readCompare(eq(4), anyString());

        verify(testScenario, times(30)).readCompare(eq(5), anyString());
        verify(testScenario, times(60)).readCompare(eq(6), anyString());
        verify(testScenario, times(30)).readCompare(eq(7), anyString());
        verify(testScenario, times(30)).readCompare(eq(8), anyString());
    }

    private String getRandomHexString(Random rand) {
        int randomValue = rand.nextInt();
        return "0x" + String.format("%08X", randomValue);
    }
}
