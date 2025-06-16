package ssd.buffer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ssd.command.CommandExecutor;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandBufferTest {

    @Mock
    CommandExecutor commandExecutor;

    CommandBuffer commandBuffer;

    @Test
    void write_6회시_flush_Process_함수를_부르는지_테스트() {
        commandBuffer = spy(new CommandBuffer(commandExecutor,new SSDArgument(new String[]{"W","0","0x12341234"})));
        commandBuffer.bufferExecutor();

        commandBuffer.setSsdArgument(new SSDArgument(new String[]{"W","1","0x12341234"}));
        commandBuffer.bufferExecutor();
        commandBuffer.setSsdArgument(new SSDArgument(new String[]{"W","2","0x12341234"}));
        commandBuffer.bufferExecutor();
        commandBuffer.setSsdArgument(new SSDArgument(new String[]{"W","3","0x12341234"}));
        commandBuffer.bufferExecutor();
        commandBuffer.setSsdArgument(new SSDArgument(new String[]{"W","4","0x12341234"}));
        commandBuffer.bufferExecutor();
        commandBuffer.setSsdArgument(new SSDArgument(new String[]{"W","5","0x12341234"}));
        commandBuffer.bufferExecutor();

        verify(commandBuffer,atLeastOnce()).flushProcess();
    }

    @Test
    void erase_6회시_flush_Process_함수를_부르는지_테스트() {
        commandBuffer = spy(new CommandBuffer(commandExecutor,new SSDArgument(new String[]{"E","0","10"})));
        commandBuffer.bufferExecutor();

        commandBuffer.setSsdArgument(new SSDArgument(new String[]{"E","10","10"}));
        commandBuffer.bufferExecutor();
        commandBuffer.setSsdArgument(new SSDArgument(new String[]{"E","20","10"}));
        commandBuffer.bufferExecutor();
        commandBuffer.setSsdArgument(new SSDArgument(new String[]{"E","30","10"}));
        commandBuffer.bufferExecutor();
        commandBuffer.setSsdArgument(new SSDArgument(new String[]{"E","40","10"}));
        commandBuffer.bufferExecutor();
        commandBuffer.setSsdArgument(new SSDArgument(new String[]{"E","50","10"}));
        commandBuffer.bufferExecutor();

        verify(commandBuffer,atLeastOnce()).flushProcess();
    }
}