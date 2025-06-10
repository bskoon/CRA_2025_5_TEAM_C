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


//    private void readCompare(int i, String s) {
//        String result = testScenario.read(i);
//        if(!result.equals(s)) fail();
//    }

    @BeforeEach
    void setUp() {
        testScenario = new TestScenario(testShell);
    }

    @Test
    void FullWriteAndReadCompare_첫번째_시나리오테스트(){
        for(int i=0;i<100;i++){
            when(testScenario.read(i)).thenReturn("0xFFFFFFFF");
        }

        assertEquals("PASS",testScenario.fullWriteAndReadCompare());
//        for(int i=0;i<20;i++){
//            for(int j=0;j<5;j++){
//                testScenario.write(i*5+j,"0xFFFFFFFF");
//                readCompare(i*5+j,"0xFFFFFFFF");
//            }
//        }
//        assertTrue(true);
    }

    @Test
    void PartialLBAWrite_두번째_시나리오테스트(){
        for(int i=0;i<150;i++){
            when(testScenario.read(i%5)).thenReturn("0xFFFFFFFF");
        }

        assertEquals("PASS", testScenario.partialLBAWrite());

//        for(int i=0;i<30;i++){
//            testScenario.write(4,"0xFFFFFFFF");
//            testScenario.write(0,"0xFFFFFFFF");
//            testScenario.write(3,"0xFFFFFFFF");
//            testScenario.write(1,"0xFFFFFFFF");
//            testScenario.write(2,"0xFFFFFFFF");
//            for(int j=0;j<5;j++){
//                readCompare(j,"0xFFFFFFFF");
//            }
//        }

    }

    @Test
    void WriteReadAging_세번째_시나리오테스트(){
        for(int i=0;i<200;i++){
            when(testScenario.read(0)).thenReturn("0xFFFFFFFF");
            when(testScenario.read(99)).thenReturn("0xFFFFFFFF");
        }

        assertEquals("PASS",testScenario.writeReadAging);

//        for(int i=0;i<200;i++){
//            testScenario.write(0,"0xFFFFFFFF");
//            readCompare(0,"0xFFFFFFFF");
//            testScenario.write(99,"0xFFFFFFFF");
//            readCompare(0,"0xFFFFFFFF");
//        }

    }
}
