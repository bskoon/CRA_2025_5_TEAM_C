package shell.command;

import shell.scenario.*;
import shell.util.SSDCaller;

import java.util.function.Function;

import static shell.util.ShellConstant.*;

public enum CommandType {
    read(2, null),
    write(3, null),
    fullread(1, null),
    fullwrite(2, null),
    erase(3, null),
    erase_range(3, null),
    flush(1, null),
    scenario1(1, Scenario1::new),
    scenario2(1, Scenario2::new),
    scenario3(1, Scenario3::new),
    scenario4(1, Scenario4::new);

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
