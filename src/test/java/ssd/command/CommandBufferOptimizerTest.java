package ssd.command;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommandBufferOptimizerTest {
    CommandBufferOptimizer commandBufferOptimizer = new CommandBufferOptimizer();

    @Test
    void makeMemory() {
        List<String> commands = List.of(
                "1_W_10_0xAAAA0000",
                "2_E_0_10",
                "3_W_9_0xFFFFFFFF",
                "4_W_8_0xFFFFFFFF",
                "5_W_7_0xFFFFFFFF"
        );

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(5, optimized.size());
        assertTrue(optimized.contains("1_W_10_0xAAAA0000"));
        assertTrue(optimized.contains("2_E_0_10"));
        assertTrue(optimized.contains("3_W_9_0xFFFFFFFF"));
        assertTrue(optimized.contains("4_W_8_0xFFFFFFFF"));
        assertTrue(optimized.contains("5_W_7_0xFFFFFFFF"));
    }

    @Test
    void testFastRead_WriteThenRead_ReturnsValue() {
        List<String> commands = List.of("1_W_10_0xAAAA0000");

        Optional<String> result = commandBufferOptimizer.fastRead(commands, 10);

        assertTrue(result.isPresent());
        assertEquals("0xAAAA0000", result.get());
    }

    @Test
    void testFastRead_EraseThenRead_ReturnsZero() {
        List<String> commands = List.of("1_E_10_2");

        Optional<String> result = commandBufferOptimizer.fastRead(commands, 10);

        assertTrue(result.isPresent());
        assertEquals("0x00000000", result.get());
    }

    @Test
    void testFastRead_OverwrittenWrite_ReturnsLatestValue() {
        List<String> commands = List.of(
                "1_W_10_0xAAAA0000",
                "2_W_10_0xBBBB1111"
        );

        Optional<String> result = commandBufferOptimizer.fastRead(commands, 10);

        assertTrue(result.isPresent());
        assertEquals("0xBBBB1111", result.get());
    }

    @Test
    void testFastRead_NoMatch_ReturnsEmpty() {
        List<String> commands = List.of("1_W_5_0xFFFEEE");

        Optional<String> result = commandBufferOptimizer.fastRead(commands, 10);

        assertTrue(result.isEmpty());
    }

    @Test
    void testIgnoreCommands_WriteErasedByErase_Removed() {
        List<String> commands = List.of(
                "1_E_18_3",
                "2_W_21_0x12341234",
                "3_E_18_5"
        );

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(1, optimized.size());
        assertEquals("1_E_18_5", optimized.get(0));
    }

    @Test
    void testMergeEraseCommands_ContinuousRange_Merged() {
        List<String> commands = List.of(
                "1_E_10_4",
                "2_E_13_3"
        );

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(1, optimized.size());
        assertEquals("1_E_10_6", optimized.get(0));
    }

    @Test
    void testMixedCommands_MergeAndWritePreserved() {
        List<String> commands = List.of(
                "1_W_20_0xAABBCCDD",
                "2_E_10_4",
                "3_E_13_3"
        );

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(2, optimized.size());
        assertTrue(optimized.contains("1_W_20_0xAABBCCDD"));
        assertTrue(optimized.contains("2_E_10_6"));
    }

    @Test
    void testFastRead_WriteAfterErase_ReturnsWrittenValue() {
        List<String> commands = List.of(
                "1_E_30_2",
                "2_W_30_0x11111111"
        );
        Optional<String> result = commandBufferOptimizer.fastRead(commands, 30);  // ✅ 30으로 수정
        assertTrue(result.isPresent());
        assertEquals("0x11111111", result.get());
    }

    @Test
    void testFastRead_ReadErasedAreaExactly_ReturnsZero() {
        List<String> commands = List.of(
                "1_W_40_0xAAAAAAAA",
                "2_E_40_1"
        );

        Optional<String> result = commandBufferOptimizer.fastRead(commands, 40);

        assertTrue(result.isPresent());
        assertEquals("0x00000000", result.get());
    }

    @Test
    void testEraseCommand_OverlappingEraseRange_MergedProperly() {
        List<String> commands = List.of(
                "1_E_5_3",
                "2_E_7_2"
        );

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(1, optimized.size());
        assertEquals("1_E_5_4", optimized.get(0)); // LBA 5~8 총 4개
    }

    @Test
    void testMultipleWrites_DifferentLBAs_AllPreserved() {
        List<String> commands = List.of(
                "1_W_10_0xAAAA0000",
                "2_W_11_0xBBBB0000",
                "3_W_12_0xCCCC0000"
        );

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(3, optimized.size());
        assertTrue(optimized.contains("1_W_10_0xAAAA0000"));
        assertTrue(optimized.contains("2_W_11_0xBBBB0000"));
        assertTrue(optimized.contains("3_W_12_0xCCCC0000"));
    }

    @Test
    void testEraseThenWrite_OnlyWriteVisible() {
        List<String> commands = List.of(
                "1_E_50_1",
                "2_W_50_0xFFF00000"
        );

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(1, optimized.size());
        assertEquals("1_W_50_0xFFF00000", optimized.get(0));
    }

    @Test
    void testErase_MaxSizeBoundaryAccepted() {
        List<String> commands = List.of("1_E_0_10");

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(1, optimized.size());
        assertEquals("1_E_0_10", optimized.get(0));
    }

    @Test
    void testErase_OverlappingWithWrite_WriteRemoved() {
        List<String> commands = List.of(
                "1_W_25_0x11111111",
                "2_E_24_3"
        );

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(1, optimized.size());
        assertEquals("1_E_24_3", optimized.get(0)); // Write to 25 should be erased
    }

    @Test
    void testFastRead_WriteAfterEraseDifferentLBA_NotAffected() {
        List<String> commands = List.of(
                "1_E_30_1",                    // LBA 30만 지움
                "2_W_31_0x11223344"           // LBA 31에 데이터 씀
        );

        Optional<String> result = CommandBufferOptimizer.fastRead(commands, 31);

        assertTrue(result.isPresent());
        assertEquals("0x11223344", result.get());
    }

    @Test
    void testMixedEraseAndWriteCommands_최대5개() {
        List<String> commands = List.of(
                "1_W_77_0x15CA21FC",
                "2_W_62_0x9F8267ED",
                "3_E_13_4",
                "4_E_79_4",
                "5_E_62_4"
        );

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertTrue(optimized.stream().anyMatch(cmd -> cmd.contains("_W_77_0x15CA21FC")));
        assertTrue(optimized.stream().anyMatch(cmd -> cmd.contains("_E_13_4")));
        assertTrue(optimized.stream().anyMatch(cmd -> cmd.contains("_E_62_4")));
        assertTrue(optimized.stream().anyMatch(cmd -> cmd.contains("_E_79_4")));

        // 삭제된 W_62 를 제외한 4개의 명령만 존재해야 함
        assertEquals(4, optimized.size());
    }

    @Test
    void testEraseAndOverwriteThenEraseAgain() {
        List<String> commands = List.of("1_E_10_2", "2_W_11_0xABCD1234", "3_E_10_3");

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(1, optimized.size());
        assertTrue(optimized.contains("1_E_10_3"));
    }

    @Test
    void testMultipleWritesThenEraseSubset() {
        List<String> commands = List.of("1_W_5_0xAAAA0000", "2_W_6_0xBBBB1111", "3_E_6_1");

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(2, optimized.size());
        assertTrue(optimized.contains("1_W_5_0xAAAA0000"));
        assertTrue(optimized.contains("2_E_6_1"));
    }

    @Test
    void testOverwriteThenEraseAll() {
        List<String> commands = List.of("1_W_20_0xAABBCCDD", "2_W_20_0x11223344", "3_E_20_1");

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(1, optimized.size());
        assertTrue(optimized.contains("1_E_20_1"));
    }

    @Test
    void testSeparateEraseBlocks() {
        List<String> commands = List.of("1_E_30_2", "2_E_33_1");

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(2, optimized.size());
        assertTrue(optimized.contains("1_E_30_2"));
        assertTrue(optimized.contains("2_E_33_1"));
    }

    @Test
    void testEraseOverlappingWriteBlock() {
        List<String> commands = List.of("1_W_40_0xAAAABBBB", "2_E_40_2");

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(1, optimized.size());
        assertTrue(optimized.contains("1_E_40_2"));
    }

    @Test
    void testWriteAfterEraseNoOverlap() {
        List<String> commands = List.of("1_E_50_2", "2_W_52_0x12345678");

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(2, optimized.size());
        assertTrue(optimized.contains("1_E_50_2"));
        assertTrue(optimized.contains("2_W_52_0x12345678"));
    }

    @Test
    void testMultipleErasesAndWrites() {
        List<String> commands = List.of("1_E_60_2", "2_W_62_0x9F8267ED", "3_E_63_1");

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(3, optimized.size());
        assertTrue(optimized.contains("1_E_60_2"));
        assertTrue(optimized.contains("2_W_62_0x9F8267ED"));
        assertTrue(optimized.contains("3_E_63_1"));
    }

    @Test
    void testEraseCompletelyOverwritesAllWrites() {
        List<String> commands = List.of("1_W_70_0xDEADBEEF", "2_E_70_1");

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(1, optimized.size());
        assertTrue(optimized.contains("1_E_70_1"));
    }

    @Test
    void testWriteThenEraseThenWriteAgain() {
        List<String> commands = List.of("1_W_80_0x00001111", "2_E_80_1", "3_W_80_0x99998888");

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(1, optimized.size());
        assertTrue(optimized.contains("1_W_80_0x99998888"));
    }

    @Test
    void testWriteBlockThenEraseSubsetAndContinueWrite() {
        List<String> commands = List.of("1_W_90_0x12341234", "2_E_90_1", "3_W_91_0x56785678");

        List<String> optimized = commandBufferOptimizer.optimize(commands);

        assertEquals(2, optimized.size());
        assertTrue(optimized.contains("1_E_90_1"));
        assertTrue(optimized.contains("2_W_91_0x56785678"));
    }
}