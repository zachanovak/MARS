package tests;

import mars.Globals;
import mars.MIPSprogram;
import mars.ProcessingException;
import mars.Settings;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class OffsetConstraintTests {
    @BeforeAll
    static void getPreValue() {
        // Init MarsLaunch so the Globals are initialized
        TestGlobals.initMarsLaunch();
        TestGlobals.offsetConstraint = Globals.getSettings().getBooleanSetting(Settings.OFFSET_CONSTRAINT);
    }

    @Test
    void shouldAssembleInstructionWithConstraint() {
        Globals.getSettings().setBooleanSetting(Settings.OFFSET_CONSTRAINT, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("lw $t0, 0($t1)");
                sourceList.add("sw $t0, 0($t1)");
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
    void shouldAssembleInstructionWithConstraint2() {
        Globals.getSettings().setBooleanSetting(Settings.OFFSET_CONSTRAINT, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add(".data");
                sourceList.add("i: .word 64");
                sourceList.add(".text");
                sourceList.add("lw $t0, i");
                sourceList.add("sw $t0, i");
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
        Globals.getSettings().setBooleanSetting(Settings.OFFSET_CONSTRAINT, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("lw $t0, ($t1)");
                sourceList.add("sw $t0, 0($t1)");
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
    void shouldNotAssembleInstructionWithConstraint2() {
        Globals.getSettings().setBooleanSetting(Settings.OFFSET_CONSTRAINT, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("lw $t0, 0($t1)");
                sourceList.add("sw $t0, ($t1)");
                return sourceList;
            }
        };
        try {
            program.tokenize();
            ArrayList programFiles = new ArrayList();
            programFiles.add(program);
            program.assemble(programFiles, true);

            Assertions.fail("Assembly failed to throw exception");
        } catch (ProcessingException ex) { }
    }

    @Test
    void shouldAssembleInstructionWithoutConstraint() {
        Globals.getSettings().setBooleanSetting(Settings.OFFSET_CONSTRAINT, false);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("lw $t0, ($t1)");
                sourceList.add("sw $t0, 0($t1)");
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
    void shouldAssembleInstructionWithoutConstraint2() {
        Globals.getSettings().setBooleanSetting(Settings.OFFSET_CONSTRAINT, false);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("lw $t0, 0($t1)");
                sourceList.add("sw $t0, ($t1)");
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
        Globals.getSettings().setBooleanSetting(Settings.OFFSET_CONSTRAINT,
                TestGlobals.offsetConstraint);
    }
}
