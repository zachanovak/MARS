package tests;

import mars.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class RegisterNameConstraintTests {

    @BeforeAll
    static void getPreValue() {
        // Init MarsLaunch so the Globals are initialized
        TestGlobals.initMarsLaunch();
        TestGlobals.registerNameConstraint = Globals.getSettings().getBooleanSetting(Settings.REGISTER_NAME_CONSTRAINT);
    }

    @Test
    void shouldAssembleInstructionWithConstraint() {
        Globals.getSettings().setBooleanSetting(Settings.REGISTER_NAME_CONSTRAINT, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("li $t1, 1");
                sourceList.add("addi $v0, $zero, 10");
                sourceList.add("syscall");
                return sourceList;
            }
        };
        try {
            program.tokenize();
            ArrayList programFiles = new ArrayList();
            programFiles.add(program);
            program.assemble(programFiles, true);
        } catch (ProcessingException ex) {
            Assertions.fail(ex);
        }
    }

    @Test
    void shouldNotAssembleInstructionWithConstraint() {
        Globals.getSettings().setBooleanSetting(Settings.REGISTER_NAME_CONSTRAINT, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("li $t1, 1");
                sourceList.add("addi $0, $zero, 10");
                sourceList.add("syscall");
                return sourceList;
            }
        };
        try {
            program.tokenize();
            ArrayList programFiles = new ArrayList();
            programFiles.add(program);
            program.assemble(programFiles, true);

            Assertions.fail("Assembly failed to throw exception");
        } catch (ProcessingException ignored) { }
    }

    @Test
    void shouldAssembleInstructionWithoutConstraint() {
        Globals.getSettings().setBooleanSetting(Settings.REGISTER_NAME_CONSTRAINT, false);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("li $t1, 1");
                sourceList.add("addi $0, $zero, 10");
                sourceList.add("syscall");
                return sourceList;
            }
        };
        try {
            program.tokenize();
            ArrayList programFiles = new ArrayList();
            programFiles.add(program);
            program.assemble(programFiles, true);
        } catch (ProcessingException ex) {
            Assertions.fail(ex);
        }
    }

    @AfterAll
    static void restorePreValue() {
        Globals.getSettings().setBooleanSetting(Settings.REGISTER_NAME_CONSTRAINT,
                TestGlobals.registerNameConstraint);
    }
}
