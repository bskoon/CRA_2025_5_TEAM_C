package shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TestScenarioTest {

    TestScenario testScenario;
    Random random;
    
    @Mock
    TestShell testShell;


    private String getRandomHexString(Random rand) {
        // 32비트 난수 생성 (0 ~ 0xFFFFFFFF)
        int randomValue = rand.nextInt(); // 기본적으로 -2^31 ~ 2^31-1 범위의 값
        // 16진수로 출력
        return "0x"+String.format("%08X", randomValue);
    }

    @BeforeEach
    void setUp() {
        testScenario = new TestScenario(testShell);
        random = new Random(1234);

    }

    @Test
    void FullWriteAndReadCompare_첫번째_시나리오테스트() throws IOException{
        for(int i=0;i<100;i++){
            when(testShell.readLBA(i)).thenReturn(getRandomHexString(random));
        }

        assertEquals("PASS",testScenario.fullWriteAndReadCompare(1234));
    }

    @Test
    void PartialLBAWrite_두번째_시나리오테스트() throws IOException{
        for(int i=0;i<150;i++){
            // write를 0xFFFFFFFF 로 고정해놓음
            when(testShell.readLBA(i%5)).thenReturn("0xFFFFFFFF");
        }

        assertEquals("PASS", testScenario.partialLBAWrite());


    }

    @Test
    void WriteReadAging_세번째_시나리오테스트() throws IOException {
        for(int i=0;i<200;i++){
            when(testShell.readLBA(0)).thenReturn(getRandomHexString(random));
            when(testShell.readLBA(99)).thenReturn(getRandomHexString(random));
        }

        assertEquals("PASS",testScenario.writeReadAging(1234));

    }
}
