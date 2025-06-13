package ssd.command;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BufferUtilTest {

    BufferUtil bufferUtil= new BufferUtil();

    @Test
    void makeMemory() {
        List<String> commands = new ArrayList<>();
        commands.add("1_W_0_0xFFFFFFFF");
        commands.add("2_E_0_10");
        commands.add("3_W_9_0xFFFFFFFF");
        commands.add("4_W_8_0xFFFFFFFF");
        commands.add("5_W_7_0xFFFFFFFF");

        HashMap<Integer,String> returnMemory = bufferUtil.makeMemory(commands);
        List<String> returnCommand = bufferUtil.makeCommand(returnMemory);

        assertEquals("1_E_0_10",returnCommand.get(0));
        assertEquals("2_W_7_0xFFFFFFFF",returnCommand.get(1));
        assertEquals("3_W_8_0xFFFFFFFF",returnCommand.get(2));
        assertEquals("4_W_9_0xFFFFFFFF",returnCommand.get(3));
    }
}