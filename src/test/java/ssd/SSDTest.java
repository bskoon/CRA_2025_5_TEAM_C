package ssd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SSDTest {
    private SSD ssd;

    @BeforeEach
    void setUp() {
        ssd = new SSD();
    }

    @Test
    void MainARGSValidCmd_소문자r_예외처리() {
        String[] ARGS = {"r", "10", "0xABCDEF12"};

        assertThrows(RuntimeException.class, () -> {
            ssd.main(ARGS);
        });
    }

    @Test
    void MainARGSValidCmd_소문자w_예외처리() {
        String[] ARGS = {"w", "10", "0xABCDEF12"};

        assertThrows(RuntimeException.class, () -> {
            ssd.main(ARGS);
        });
    }

    @Test
    void MainARGSValidCmd_소문자특수문자_예외처리() {
        String[] ARGS = {"#", "10", "0xABCDEF12"};

        assertThrows(RuntimeException.class, () -> {
            ssd.main(ARGS);
        });
    }

    @Test
    void MainARGSValidCmd_소문자빈값_예외처리() {
        String[] ARGS = {"", "10", "0xABCDEF12"};

        assertThrows(RuntimeException.class, () -> {
            ssd.main(ARGS);
        });
    }

    @Test
    void WriteValidLBA_마이너스_예외처리() {
        assertThrows(RuntimeException.class, () -> {
            ssd.write(-1, "0x12345678");
        });
    }

    @Test
    void WriteValidLBA_100이상_예외처리() {
        assertThrows(RuntimeException.class, () -> {
            ssd.write(100, "0x12345678");
        });
    }

    @Test
    void WriteValidValue_Ox없을때_예외처리() {
        assertThrows(RuntimeException.class, () -> {
            ssd.write(10, "12345678");
        });
    }

    @Test
    void WriteValidValue_헥사벗어났을때_예외처리() {
        assertThrows(RuntimeException.class, () -> {
            ssd.write(10, "0xZZZZZZZZ");
        });
    }

    @Test
    void WriteValidValue_10자리아닐때_예외처리() {
        assertThrows(RuntimeException.class, () -> {
            assertFalse(ssd.write(10, "0x123"));
        });
    }

    @Test
    void Write_테스트() {
        boolean result = ssd.write(10, "0xABCDEF01");
        assertTrue(result);
        assertEquals("0xABCDEF01", ssd.read(10));
    }

    @Test
    void ReadValidLBA_마이너스_예외처리() {
        assertThrows(RuntimeException.class, () -> {
            ssd.read(-1);
        });
    }

    @Test
    void ReadValidLBA_100이상_예외처리() {
        assertThrows(RuntimeException.class, () -> {
            ssd.read(100);
        });
    }

    @Test
    void Read_테스트() {
        ssd.write(5, "0xABCDEF01");
        assertEquals("0xABCDEF01", ssd.read(5));
    }
}