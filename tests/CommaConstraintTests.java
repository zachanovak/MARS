package tests;

import mars.Globals;
import mars.MarsLaunch;
import mars.Settings;
import mars.assembler.TokenList;
import mars.assembler.TokenTypes;
import mars.assembler.Tokenizer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CommaConstraintTests
{
    // Used to restore existing value after tests mess with the value
    private static boolean commaConstraint;

    @BeforeAll
    static void getPreValue() {
        // Init MarsLaunch so we Globals are initialized
        MarsLaunch marsLaunch = new MarsLaunch(new String[]{});
        commaConstraint = Globals.getSettings().getBooleanSetting(Settings.COMMA_CONSTRAINT);
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

    @AfterAll
    static void restorePreValue() {
        Globals.getSettings().setBooleanSetting(Settings.COMMA_CONSTRAINT, commaConstraint);
    }
}
