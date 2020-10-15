package tests;

import mars.*;
import mars.assembler.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CommaConstraintTests {

    @BeforeAll
    static void getPreValue() {
        // Init MarsLaunch so the Globals are initialized
        TestGlobals.initMarsLaunch();
        TestGlobals.commaConstraint = Globals.getSettings().getBooleanSetting(Settings.COMMA_CONSTRAINT);
    }

    @Test
    void shouldTokenizeCorrectNumCommas1() {
        Globals.getSettings().setBooleanSetting(Settings.COMMA_CONSTRAINT, true);
        Tokenizer tokenizer = new Tokenizer();
        String line = "li $t1, 1";
        int numOfCommaTokens = 0;

        TokenList tokens = tokenizer.tokenizeLine(0, line);
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenTypes.COMMA)
                numOfCommaTokens++;
        }

        Assertions.assertEquals(numOfCommaTokens, 1);
    }

    @Test
    void shouldTokenizeCorrectNumCommas2() {
        Globals.getSettings().setBooleanSetting(Settings.COMMA_CONSTRAINT, true);
        Tokenizer tokenizer = new Tokenizer();
        String line = "add $t0, $t0, $t1";
        int numOfCommaTokens = 0;

        TokenList tokens = tokenizer.tokenizeLine(0, line);
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenTypes.COMMA)
                numOfCommaTokens++;
        }

        Assertions.assertEquals(numOfCommaTokens, 2);
    }

    @Test
    void shouldTokenizeCorrectNumCommas3() {
        Globals.getSettings().setBooleanSetting(Settings.COMMA_CONSTRAINT, true);
        Tokenizer tokenizer = new Tokenizer();
        String line = "syscall";
        int numOfCommaTokens = 0;

        TokenList tokens = tokenizer.tokenizeLine(0, line);
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenTypes.COMMA)
                numOfCommaTokens++;
        }

        Assertions.assertEquals(numOfCommaTokens, 0);
    }

    @Test
    void shouldNotTokenizeCommas() {
        Globals.getSettings().setBooleanSetting(Settings.COMMA_CONSTRAINT, false);
        Tokenizer tokenizer = new Tokenizer();
        String line = "add $t0, $t0, $t1";
        int numOfCommaTokens = 0;

        TokenList tokens = tokenizer.tokenizeLine(0, line);
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType() == TokenTypes.COMMA)
                numOfCommaTokens++;
        }

        Assertions.assertEquals(numOfCommaTokens, 0);
    }

    @Test
    void shouldAssembleInstructionWithConstraint() {
        Globals.getSettings().setBooleanSetting(Settings.COMMA_CONSTRAINT, true);
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
        Globals.getSettings().setBooleanSetting(Settings.COMMA_CONSTRAINT, true);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("li $t1, 1");
                sourceList.add("addi $v0, $zero 10");
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
        } catch (ProcessingException ex) { }
    }

    @Test
    void shouldAssembleInstructionWithoutConstraint() {
        Globals.getSettings().setBooleanSetting(Settings.COMMA_CONSTRAINT, false);
        MIPSprogram program = new MIPSprogram() {
            @Override
            public ArrayList getSourceList() {
                ArrayList sourceList = new ArrayList();
                sourceList.add("li $t1, 1");
                sourceList.add("addi $v0, $zero 10");
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
        Globals.getSettings().setBooleanSetting(Settings.COMMA_CONSTRAINT,
                TestGlobals.commaConstraint);
    }
}
