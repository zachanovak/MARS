package tests;

import mars.Globals;
import mars.venus.AutoLayoutUtilities;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AutoLayoutTests {
    // TODO:
    // Test if instructions are formatted correctly
    // Test if in-line comments are formatted correctly
    // Test if existing in-line comments with tooons of space in between the instruction and the comment get fixed
    // Test if comments on their own line
    // Test if recognizes the difference between a label and a label in a comment
    // Test if ignores any symbols in a string literal
    // Test if works on one line program
    // Test with different tab sizes

    @BeforeAll
    static void getPreValue() {
        // Init MarsLaunch so the Globals are initialized
        TestGlobals.initMarsLaunch();
        TestGlobals.tabSize = Globals.getSettings().getEditorTabSize();
    }

    @Test()
    void shouldFormatLabelsCorrectly() {
        String sourceCode =
                "LABEL:\n" +
                "second_label:";
        Assertions.assertEquals(
                "LABEL:\n" +
                "second_label:", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldFormatLabelsCorrectly2() {
        String sourceCode =
                "       LABEL:\n" +
                "                       second_label:";
        Assertions.assertEquals(
                "LABEL:\n" +
                "second_label:", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldFormatInstructionsCorrectly() {
        int tabSize = Globals.getSettings().getEditorTabSize();
        String tab = new String(new char[tabSize]).replace('\0', ' ');

        String sourceCode =
                "addi $t0, $t0, 1\n" +
                "j LABEL";
        Assertions.assertEquals(
                tab + "addi $t0, $t0, 1\n" +
                tab + "j LABEL", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldFormatInstructionsCorrectly2() {
        int tabSize = Globals.getSettings().getEditorTabSize();
        String tab = new String(new char[tabSize]).replace('\0', ' ');

        String sourceCode =
                "               addi $t0, $t0, 1\n" +
                "   j LABEL";
        Assertions.assertEquals(
                tab + "addi $t0, $t0, 1\n" +
                tab + "j LABEL", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @AfterAll
    static void restorePreValue() {
        Globals.getSettings().setEditorTabSize(TestGlobals.tabSize);
    }
}
