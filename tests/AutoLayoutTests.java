package tests;

import mars.venus.AutoLayoutUtilities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AutoLayoutTests {
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
        String sourceCode =
                "addi $t0, $t0, 1\n" +
                "j LABEL";
        Assertions.assertEquals(
                "\taddi $t0, $t0, 1\n" +
                "\tj LABEL", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldFormatInstructionsCorrectly2() {
        String sourceCode =
                "               addi $t0, $t0, 1\n" +
                "   j LABEL";
        Assertions.assertEquals(
                "\taddi $t0, $t0, 1\n" +
                "\tj LABEL", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldFormatLabelsAndInstructionsCorrectly() {
        String sourceCode =
                "LABEL:\n" +
                "addi $t0, $t0, 1";
        Assertions.assertEquals(
                "LABEL:\n" +
                        "\taddi $t0, $t0, 1", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldFormatLabelsAndInstructionsCorrectly2() {
        String sourceCode =
                "                  LABEL:\n" +
                        "        addi $t0, $t0, 1";
        Assertions.assertEquals(
                "LABEL:\n" +
                        "\taddi $t0, $t0, 1", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldFormatOneLineProgramCorrectly() {
        String sourceCode =
                "LABEL:";
        Assertions.assertEquals(
                "LABEL:", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldFormatOneLineProgramCorrectly2() {
        String sourceCode =
                "addi $t0, $t0, 1";
        Assertions.assertEquals(
                "\taddi $t0, $t0, 1", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldFormatInlineCommentCorrectly() {
        String sourceCode =
                "LABEL:# Comment";
        Assertions.assertEquals(
                "LABEL:\t# Comment", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldFormatInlineCommentCorrectly2() {
        String sourceCode =
                "addi $t0, $t0, 1# Comment";
        Assertions.assertEquals(
                "\taddi $t0, $t0, 1\t# Comment", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldFormatInlineCommentCorrectly3() {
        String sourceCode =
                "   LABEL:                 # Comment   ";
        Assertions.assertEquals(
                "LABEL:\t# Comment", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldNotFormatSeparateLineComment() {
        String sourceCode =
                "# Comment";
        Assertions.assertEquals(
                "# Comment", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldNotFormatSeparateLineComment2() {
        String sourceCode =
                "                # Comment";
        Assertions.assertEquals(
                "                # Comment", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldIgnoreSymbolsInCommentsAndFormatCorrectly() {
        String sourceCode =
                "LABEL:# Comment # Not a second comment";
        Assertions.assertEquals(
                "LABEL:\t# Comment # Not a second comment", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldIgnoreSymbolsInCommentsAndFormatCorrectly2() {
        String sourceCode =
                "addi $t0, $t0, 1# Comment and fake label:";
        Assertions.assertEquals(
                "\taddi $t0, $t0, 1\t# Comment and fake label:", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldIgnoreSymbolsInCommentsAndFormatCorrectly3() {
        String sourceCode =
                "addi $t0, $t0, 1# Comment and fake label':'with '' quotes '";
        Assertions.assertEquals(
                "\taddi $t0, $t0, 1\t# Comment and fake label':'with '' quotes '", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldIgnoreSymbolsInStringsAndFormatCorrectly() {
        String sourceCode =
                ".asciiz \"This isn't an actual # comment\"";
        Assertions.assertEquals(
                "\t.asciiz \"This isn't an actual # comment\"", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldIgnoreSymbolsInStringsAndFormatCorrectly2() {
        String sourceCode =
                ".asciiz \"This isn't an actual # comment\"# This is real";
        Assertions.assertEquals(
                "\t.asciiz \"This isn't an actual # comment\"\t# This is real", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldIgnoreSymbolsInStringsAndFormatCorrectly3() {
        String sourceCode =
                ".asciiz \"This isn't 'an' actual' # comment\"# This is real";
        Assertions.assertEquals(
                "\t.asciiz \"This isn't 'an' actual' # comment\"\t# This is real", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldIgnoreSymbolsInStringsAndFormatCorrectly4() {
        String sourceCode =
                ".asciiz \"This isn't an actual label:\"";
        Assertions.assertEquals(
                "\t.asciiz \"This isn't an actual label:\"", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldIgnoreSymbolsInStringsAndFormatCorrectly5() {
        String sourceCode =
                ".asciiz \"Escaped quote \\\" This isn't an actual label:\"";
        Assertions.assertEquals(
                "\t.asciiz \"Escaped quote \\\" This isn't an actual label:\"", AutoLayoutUtilities.autoLayout(sourceCode));
    }


    @Test()
    void shouldIgnoreSymbolsInBytesAndFormatCorrectly() {
        String sourceCode =
                ".byte '#'# Comment";
        Assertions.assertEquals(
                "\t.byte '#'\t# Comment", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldIgnoreSymbolsInBytesAndFormatCorrectly2() {
        String sourceCode =
                ".byte ':'";
        Assertions.assertEquals(
                "\t.byte ':'", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test()
    void shouldIgnoreSymbolsInBytesAndFormatCorrectly3() {
        String sourceCode =
                ".byte '\"'# Comment";
        Assertions.assertEquals(
                "\t.byte '\"'\t# Comment", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test
    void shouldFormatMultipleInlineCommentsCorrectly() {
        String sourceCode =
                "LABEL:# Comment\n" +
                "SECOND_LABEL:# Comment";
        Assertions.assertEquals(
                "LABEL:       \t# Comment\n" +
                "SECOND_LABEL:\t# Comment", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test
    void shouldFormatMultipleInlineCommentsCorrectly2() {
        String sourceCode =
                "addi $t0, $t0, 1# Comment\n" +
                "j LABEL# Comment";
        Assertions.assertEquals(
                "\taddi $t0, $t0, 1\t# Comment\n" +
                "\tj LABEL         \t# Comment", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test
    void shouldFormatMultipleInlineCommentsCorrectly3() {
        String sourceCode =
                "LABEL:# Comment\n" +
                "j LABEL# Comment";
        Assertions.assertEquals(
                "LABEL:  \t# Comment\n" +
                "\tj LABEL\t# Comment", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test
    void shouldFormatMultipleInlineCommentsCorrectly4() {
        String sourceCode =
                "BIGGER_LABEL:# Comment\n" +
                "j LABEL# Comment";
        Assertions.assertEquals(
                "BIGGER_LABEL:\t# Comment\n" +
                "\tj LABEL     \t# Comment", AutoLayoutUtilities.autoLayout(sourceCode));
    }

    @Test
    void shouldFormatMultipleInlineCommentsCorrectly5() {
        String sourceCode =
                "LABEL:# Comment\n" +
                "addi $t0, $t0, 1# Comment\n" +
                "j LABEL# Comment";
        Assertions.assertEquals(
                "LABEL:           \t# Comment\n" +
                "\taddi $t0, $t0, 1\t# Comment\n" +
                "\tj LABEL         \t# Comment", AutoLayoutUtilities.autoLayout(sourceCode));
    }
}
