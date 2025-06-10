package ssd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SSDTest {
    private SSD ssd;

    @BeforeEach
    void setUp() {
        ssd = new SSD();
    }

    @Test
    void testValidWriteCommandLowerR() {
        String[] args = {"r", "10", "0xABCDEF12"};

        assertThrows(RuntimeException.class, () -> {
            ssd.main(args);
        });
    }

    @Test
    void testValidWriteCommandLowerW() {
        String[] args = {"w", "10", "0xABCDEF12"};

        assertThrows(RuntimeException.class, () -> {
            ssd.main(args);
        });
    }

    @Test
    void testValidWriteCommandHash() {
        String[] args = {"#", "10", "0xABCDEF12"};

        assertThrows(RuntimeException.class, () -> {
            ssd.main(args);
        });
    }

    @Test
    void testValidWriteCommandEmpty() {
        String[] args = {"", "10", "0xABCDEF12"};

        assertThrows(RuntimeException.class, () -> {
            ssd.main(args);
        });
    }

    @Test
    void testWriteInvalidLBAMinus() {
        assertThrows(RuntimeException.class, () -> {
            ssd.write(-1, "0x12345678");
        });
    }

    @Test
    void testWriteInvalidLBA100Higher() {
        assertThrows(RuntimeException.class, () -> {
            ssd.write(100, "0x12345678");
        });
    }

    @Test
    void testWriteInvalidValueFormatNotOx() {
        assertThrows(RuntimeException.class, () -> {
            ssd.write(10, "12345678");
        });
    }

    @Test
    void testWriteInvalidValueFormatNotHex() {
        assertThrows(RuntimeException.class, () -> {
            ssd.write(10, "0xZZZZZZZZ");
        });
    }

    @Test
    void testWriteInvalidValueFormatShort() {
        assertThrows(RuntimeException.class, () -> {
            assertFalse(ssd.write(10, "0x123"));
        });
    }

    @Test
    void testWriteValid() {
        boolean result = ssd.write(10, "0xABCDEF01");
        assertTrue(result);
        assertEquals("0xABCDEF01", ssd.read(10));
    }

    @Test
    void testReadValid() {
        ssd.write(5, "0xABCDEF01");
        assertEquals("0xABCDEF01", ssd.read(5));
    }
}