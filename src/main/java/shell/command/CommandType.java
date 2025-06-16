package shell.command;

import shell.scenario.*;
import shell.util.SSDCaller;

import java.util.function.Function;

import static shell.util.ShellConstant.*;

public enum CommandType {
    read(READ_ARG_COUNT, null),
    write(WRITE_ARG_COUNT, null),
    fullread(FULLREAD_ARG_COUNT, null),
    fullwrite(FULLWRITE_ARG_COUNT, null),
    erase(ERASE_ARG_COUNT, null),
    erase_range(ERASE_ARG_COUNT, null),
    flush(FLUSH_ARG_COUNT, null),
    scenario1(SCENARIO_ARG_COUNT, Scenario1::new),
    scenario2(SCENARIO_ARG_COUNT, Scenario2::new),
    scenario3(SCENARIO_ARG_COUNT, Scenario3::new),
    scenario4(SCENARIO_ARG_COUNT, Scenario4::new);

    private final int argCount;
    private final Function<SSDCaller, TestScenario> testScenario;


    CommandType(int argCount, Function<SSDCaller, TestScenario> testScenario) {
        this.argCount = argCount;
        this.testScenario = testScenario;
    }

    public static CommandType fromString(String value) {
        if (value == null) {
            throw new RuntimeException("명령 문자열이 null입니다.");
        }

        return switch (value) {
            case READ -> read;
            case WRITE -> write;
            case FULLREAD -> fullread;
            case FULLWRITE -> fullwrite;
            case ERASE -> erase;
            case ERASERANGE -> erase_range;
            case FLUSH -> flush;
            case SCENARIO_1 -> scenario1;
            case SCENARIO_2 -> scenario2;
            case SCENARIO_3 -> scenario3;
            case SCENARIO_4 -> scenario4;
            default -> throw new RuntimeException("알 수 없는 명령: " + value);
        };
    }

    public int getArgCount() {
        return argCount;
    }

    public TestScenario getScenario(SSDCaller caller) {
        return testScenario.apply(caller);
    }
}
