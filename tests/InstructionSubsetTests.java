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

public class InstructionSubsetTests {
    // TODO:
    // Test if saving and getting instruction subset from Settings' preferences works

    @BeforeAll
    static void getPreValue() {
        // Init MarsLaunch so the Globals are initialized
        TestGlobals.initMarsLaunch();
        TestGlobals.instructionSubset = Globals.getSettings().getBooleanSetting(Settings.INSTRUCTION_SUBSET);
        TestGlobals.whitelist = Globals.getSettings().getBooleanSetting(Settings.INSTRUCTION_SUBSET_WHITELIST);
        TestGlobals.instructionSubsetString = Globals.getSettings().getInstructionSubsetString();
    }

    @Test
    void shouldAssembleInstructionWithWhitelistConstraint() {
        Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET, true);
        Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET_WHITELIST, true);
        Settings.instructionSubset = new ArrayList<>();
        Settings.instructionSubset.add("lw");
        Settings.instructionSubset.add("jr");
        Settings.instructionSubset.add("addi");

        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("lw $t0, 0($t1)");
                sourceList.add("addi $t0, $t0, 1");
                sourceList.add("jr $ra");
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
    void shouldNotAssembleInstructionWithWhitelistConstraint() {
        Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET, true);
        Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET_WHITELIST, true);
        Settings.instructionSubset = new ArrayList<>();
        Settings.instructionSubset.add("lw");
        Settings.instructionSubset.add("jr");

        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("lw $t0, 0($t1)");
                sourceList.add("addi $t0, $t0, 1");
                sourceList.add("jr $ra");
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
    void shouldAssembleInstructionWithBlacklistConstraint() {
        Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET, true);
        Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET_WHITELIST, false);
        Settings.instructionSubset = new ArrayList<>();
        Settings.instructionSubset.add("add");
        Settings.instructionSubset.add("blt");
        Settings.instructionSubset.add("j");

        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("lw $t0, 0($t1)");
                sourceList.add("addi $t0, $t0, 1");
                sourceList.add("jr $ra");
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
    void shouldAssembleInstructionWithBlacklistConstraint2() {
        Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET, true);
        Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET_WHITELIST, false);
        Settings.instructionSubset = new ArrayList<>();
        Settings.instructionSubset.add("add");

        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("add:");
                sourceList.add("blt $t0, $t0, add");
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
    void shouldNotAssembleInstructionWithBlacklistConstraint() {
        Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET, true);
        Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET_WHITELIST, false);
        Settings.instructionSubset = new ArrayList<>();
        Settings.instructionSubset.add("add");
        Settings.instructionSubset.add("blt");
        Settings.instructionSubset.add("jr");

        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("lw $t0, 0($t1)");
                sourceList.add("addi $t0, $t0, 1");
                sourceList.add("jr $ra");
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
    void shouldStoreAndReceiveCorrectSubset() {
        String subset = "add~addi~lw~li~blt";
        Globals.getSettings().setInstructionSubsetString(subset);
        String storedSubset = Globals.getSettings().getInstructionSubsetString();

        Assertions.assertEquals(subset, storedSubset);
    }

    @AfterAll
    static void restorePreValue() {
        Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET,
                TestGlobals.instructionSubset);
        Globals.getSettings().setBooleanSetting(Settings.INSTRUCTION_SUBSET_WHITELIST,
                TestGlobals.whitelist);
        Globals.getSettings().setInstructionSubsetString(TestGlobals.instructionSubsetString);
    }
}
