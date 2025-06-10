package shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TestScenarioTest {

    TestScenario testScenario;
    
    @Mock
    TestShell testShell;


    @BeforeEach
    void setUp() {
        testScenario = new TestScenario(testShell);
    }

    @Test
    void FullWriteAndReadCompare_첫번째_시나리오테스트(){
        for(int i=0;i<100;i++){
            // write를 0xFFFFFFFF 로 고정해놓음
            when(testScenario.read(i)).thenReturn("0xFFFFFFFF");
        }

        assertEquals("PASS",testScenario.fullWriteAndReadCompare());
    }

    @Test
    void PartialLBAWrite_두번째_시나리오테스트(){
        for(int i=0;i<150;i++){
            // write를 0xFFFFFFFF 로 고정해놓음
            when(testScenario.read(i%5)).thenReturn("0xFFFFFFFF");
        }

        assertEquals("PASS", testScenario.partialLBAWrite());


    }

    @Test
    void WriteReadAging_세번째_시나리오테스트(){
        for(int i=0;i<200;i++){
            // write를 0xFFFFFFFF 로 고정해놓음
            when(testScenario.read(0)).thenReturn("0xFFFFFFFF");
            when(testScenario.read(99)).thenReturn("0xFFFFFFFF");
        }

        assertEquals("PASS",testScenario.writeReadAging());

    }
}
