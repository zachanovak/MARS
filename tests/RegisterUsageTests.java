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

public class RegisterUsageTests {

    @BeforeAll
    static void getPreValue() {
        // Init MarsLaunch so the Globals are initialized
        TestGlobals.initMarsLaunch();
        TestGlobals.registerUsage = Globals.getSettings().getBooleanSetting(Settings.POPUP_REGISTER_USAGE);
    }

    @Test
    void shouldBeNull() {
        Globals.getSettings().setBooleanSetting(Settings.POPUP_REGISTER_USAGE, false);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("li $t1, 1");
                sourceList.add("addi $t0, $zero, 10");
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
        Assertions.assertNull(Settings.registerUsageMap);
    }

    @Test
    void shouldHaveRightAmountOfKeys() {
        Globals.getSettings().setBooleanSetting(Settings.POPUP_REGISTER_USAGE, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("li $t1, 1");
                sourceList.add("addi $t0, $zero, 10");
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
        Assertions.assertNotNull(Settings.registerUsageMap);
        Assertions.assertEquals(3, Settings.registerUsageMap.keySet().size());
    }

    @Test
    void shouldHaveRightAmountOfKeys2() {
        Globals.getSettings().setBooleanSetting(Settings.POPUP_REGISTER_USAGE, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("addi $t0, $zero, 10");
                sourceList.add("move $t1, $t2");
                sourceList.add("move $t3, $t4");
                sourceList.add("move $t5, $t6");
                sourceList.add("move $t7, $t8");
                sourceList.add("move $t9, $s0");
                sourceList.add("move $s1, $s2");
                sourceList.add("move $s3, $s4");
                sourceList.add("move $s5, $s6");
                sourceList.add("move $s7, $at");
                sourceList.add("move $v0, $v1");
                sourceList.add("move $a0, $a1");
                sourceList.add("move $a2, $a3");
                sourceList.add("move $k0, $k1");
                sourceList.add("move $gp, $sp");
                sourceList.add("move $fp, $ra");
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
        Assertions.assertNotNull(Settings.registerUsageMap);
        Assertions.assertEquals(32, Settings.registerUsageMap.keySet().size());
    }

    @Test
    void shouldHaveRightKeys() {
        Globals.getSettings().setBooleanSetting(Settings.POPUP_REGISTER_USAGE, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("li $t1, 1");
                sourceList.add("addi $t0, $zero, 10");
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
        Assertions.assertNotNull(Settings.registerUsageMap);
        Assertions.assertTrue(Settings.registerUsageMap.containsKey("$t1"));
        Assertions.assertTrue(Settings.registerUsageMap.containsKey("$t0"));
        Assertions.assertTrue(Settings.registerUsageMap.containsKey("$zero"));
    }

    @Test
    void shouldHaveRightKeys2() {
        Globals.getSettings().setBooleanSetting(Settings.POPUP_REGISTER_USAGE, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("addi $t0, $zero, 10");
                sourceList.add("move $t1, $t2");
                sourceList.add("move $t3, $t4");
                sourceList.add("move $t5, $t6");
                sourceList.add("move $t7, $t8");
                sourceList.add("move $t9, $s0");
                sourceList.add("move $s1, $s2");
                sourceList.add("move $s3, $s4");
                sourceList.add("move $s5, $s6");
                sourceList.add("move $s7, $at");
                sourceList.add("move $v0, $v1");
                sourceList.add("move $a0, $a1");
                sourceList.add("move $a2, $a3");
                sourceList.add("move $k0, $k1");
                sourceList.add("move $gp, $sp");
                sourceList.add("move $fp, $ra");
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
        Assertions.assertNotNull(Settings.registerUsageMap);
        String[] allRegs = {
                "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9",
                "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7",
                "$v0", "$v1", "$a0", "$a1", "$a2", "$a3", "$sp", "$fp", "$ra",
                "$zero", "$at", "$k0", "$k1", "$gp"
        };
        boolean allRegsAreKeys = true;
        for (String reg : allRegs) {
            if (!Settings.registerUsageMap.containsKey(reg)) {
                allRegsAreKeys = false;
                break;
            }
        }
        Assertions.assertTrue(allRegsAreKeys);
    }

    @Test
    void shouldHaveRightValues() {
        Globals.getSettings().setBooleanSetting(Settings.POPUP_REGISTER_USAGE, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("li $t0, 1");
                sourceList.add("addi $t0, $zero, 10");
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
        Assertions.assertNotNull(Settings.registerUsageMap);
        Assertions.assertEquals(2, Settings.registerUsageMap.get("$t0"));
        Assertions.assertEquals(1, Settings.registerUsageMap.get("$zero"));
    }

    @Test
    void shouldHaveRightValues2() {
        Globals.getSettings().setBooleanSetting(Settings.POPUP_REGISTER_USAGE, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("addi $t0, $zero, 10");
                sourceList.add("move $t1, $t2");
                sourceList.add("move $t3, $t4");
                sourceList.add("move $t5, $t6");
                sourceList.add("move $t7, $t8");
                sourceList.add("move $t9, $s0");
                sourceList.add("move $s1, $s2");
                sourceList.add("move $s3, $s4");
                sourceList.add("move $s5, $s6");
                sourceList.add("move $s7, $at");
                sourceList.add("move $v0, $v1");
                sourceList.add("move $a0, $a1");
                sourceList.add("move $a2, $a3");
                sourceList.add("move $k0, $k1");
                sourceList.add("move $gp, $sp");
                sourceList.add("move $fp, $ra");
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
        Assertions.assertNotNull(Settings.registerUsageMap);
        String[] allRegs = {
                "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$t8", "$t9",
                "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7",
                "$v0", "$v1", "$a0", "$a1", "$a2", "$a3", "$sp", "$fp", "$ra",
                "$zero", "$at", "$k0", "$k1", "$gp"
        };
        boolean allRegsAreCountedOnce = true;
        for (String reg : allRegs) {
            if (Settings.registerUsageMap.get(reg) != 1) {
                allRegsAreCountedOnce = false;
                break;
            }
        }
        Assertions.assertTrue(allRegsAreCountedOnce);
    }

    @Test
    void shouldNotCountRegistersInComments() {
        Globals.getSettings().setBooleanSetting(Settings.POPUP_REGISTER_USAGE, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("li $t0, 1 # $t0 now holds the value 1");
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
        Assertions.assertNotNull(Settings.registerUsageMap);
        Assertions.assertEquals(1, Settings.registerUsageMap.get("$t0"));
    }

    @Test
    void shouldNotCountRegistersMultipleTimesFromLoops() {
        Globals.getSettings().setBooleanSetting(Settings.POPUP_REGISTER_USAGE, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("LOOP:");
                sourceList.add("li $t0, 1");
                sourceList.add("j LOOP");
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
        Assertions.assertNotNull(Settings.registerUsageMap);
        Assertions.assertEquals(1, Settings.registerUsageMap.get("$t0"));
    }

    @Test
    void shouldNotCountValuesFromOldPrograms() {
        Globals.getSettings().setBooleanSetting(Settings.POPUP_REGISTER_USAGE, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("li $t0, 1");
                return sourceList;
            }
        };
        try {
            program.tokenize();
            ArrayList programFiles = new ArrayList();
            programFiles.add(program);
            program.assemble(programFiles, true);

            // "New" program is assembled here, should rewrite old values
            program.tokenize();
            programFiles = new ArrayList();
            programFiles.add(program);
            program.assemble(programFiles, true);
        } catch (ProcessingException ex) {
            Assertions.fail(ex);
        }
        Assertions.assertNotNull(Settings.registerUsageMap);
        Assertions.assertEquals(1, Settings.registerUsageMap.get("$t0"));
    }

    @AfterAll
    static void restorePreValue() {
        Globals.getSettings().setBooleanSetting(Settings.POPUP_REGISTER_USAGE,
                TestGlobals.registerUsage);
    }
}
