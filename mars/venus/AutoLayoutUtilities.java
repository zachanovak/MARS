package mars.venus;

import mars.Globals;

public class AutoLayoutUtilities {

    private AutoLayoutUtilities() {}

    public static String autoLayout(String source) {
        StringBuilder newSource = new StringBuilder();
        String line;
        // For the furthest '#' symbol
        int furthestIndex = -1;
        int newLineIndex = source.indexOf('\n');

        // FIRST PASS
        // Format  all labels and instructions and figure out the furthest '#' symbol
        while (newLineIndex != -1) {

            line = source.substring(0, newLineIndex);

            // If the line is not just a comment. The second part of the condition checks if everything
            // before the '#' symbol is whitespace.
            if (!line.contains("#") || !line.substring(0, line.indexOf('#')).trim().isEmpty()) {
                // After determing the line is not just a comment we can trim the excess whitespace
                line = fixLine(line.trim());
                // We want to keep the whitespace the comment has if it's by itself
                // Also we only want to track the furthest index of comments on lines w/ instructions
                furthestIndex = Math.max(getCommentIndex(line), furthestIndex);
            }

            newSource.append(line);
            newSource.append('\n');

            source = source.substring(newLineIndex + 1);
            newLineIndex = source.indexOf('\n');
        }
        line = source;

        if (!line.contains("#") || !line.substring(0, line.indexOf('#')).trim().isEmpty()) {
            line = fixLine(line.trim());
            furthestIndex = Math.max(getCommentIndex(line), furthestIndex);
        }
        newSource.append(line);


        String firstPass = newSource.toString();
        newSource = new StringBuilder();
        newLineIndex = firstPass.indexOf('\n');
        // Get tab size so spaces can be added instead of tabs as tabs cause alignment issues
        int tabSize = Globals.getSettings().getEditorTabSize();
        String tab = new String(new char[tabSize]).replace('\0', ' ');
        // SECOND PASS
        // Format all in-line comments with the furthest index plus a tab.
        while (newLineIndex != -1) {
            line = firstPass.substring(0, newLineIndex);

            // If the line has a comment, and it isn't only just a comment
            int commentIndex = getCommentIndex(line);
            if (commentIndex != -1 && !line.substring(0, commentIndex).trim().isEmpty() ){
                // Set the comment to the index of furthest plus a tab
                // String space is to add however much space is needed between instruction and comment
                String space = new String(new char[furthestIndex - line.substring(0, commentIndex).length()]).replace('\0', ' ');
                line = line.substring(0, commentIndex) + space + tab + line.substring(commentIndex);
            }

            newSource.append(line);
            newSource.append('\n');

            firstPass = firstPass.substring(newLineIndex + 1);
            newLineIndex = firstPass.indexOf('\n');
        }
        line = firstPass;
        int commentIndex = getCommentIndex(line);
        if (commentIndex != -1 && !line.substring(0, commentIndex).trim().isEmpty()) {
            String space = new String(new char[furthestIndex - line.substring(0, commentIndex).length()]).replace('\0', ' ');
            line = line.substring(0, commentIndex) + space + tab + line.substring(commentIndex);
        }
        newSource.append(line);

        return newSource.toString();
    }

    private static String fixLine(String source) {
        if (source.isEmpty() || source.equals("\n")) {
            // If nothing in line, nothing needs fixing
            return source;
        } else {
            // If line is anything other than a label, it needs to be indented
            // Also if it has a comment, cut out the space between the instruction and comment
            // for sake of the second pass.

            int commentIndex = getCommentIndex(source);
            if (commentIndex != -1) {
                source = source.substring(0, commentIndex).trim() + source.substring(commentIndex);
            }

            if (isLineALabel(source)) {
                return source;
            } else {
                return new String(new char[Globals.getSettings().getEditorTabSize()])
                        .replace('\0', ' ') + source;
            }
        }
    }

    /**
     * Checks if the line is a label
     * @param line to check if is label
     * @return true if line is a label, false if not
     */
    private static boolean isLineALabel(String line) {
        // Can't just use source.contains(":") to see if line is a label, because the ':'
        // symbol could be inside a string literal, a byte, or a comment
        // If the first colon we find occurs before the first quote or comment, then its a label
        // If we find a quote, escape character, or comment first then it needs to be indented
        // Afterwards we don't find any quotes, escaped characters, comments, or colons then indent like any instuction
        for (int i = 0; i < line.length(); i++) {
            switch (line.charAt(i)) {
                case '\\':
                case '"':
                case '\'':
                case '#':
                    return false;
                case ':':
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets the index where the '#' symbol starts in a comment
     * @param line to check for comment
     * @return index of '#' symbol in a comment, -1 if no comment found
     */
    private static int getCommentIndex(String line) {
        // Can't just use line.contains("#") to check if it has a comment because the
        // symbol could just be inside of a string literal or a byte
        boolean inside = false; // Used to determine if inside String/byte
        char insideWhat = '\0'; // Used to determine if inside single or double quotes
        boolean escaped = false; // Used to determine if the current character was escaped with a '\' beforehand
        for (int i = 0; i < line.length(); i++) {
            switch (line.charAt(i)) {
                case '\\':
                    escaped = true;
                    break;
                case '"':
                case '\'':
                    if (!escaped && insideWhat == '\0') {
                        insideWhat = line.charAt(i);
                        inside = true;
                    } else if (!escaped && insideWhat == line.charAt(i)) {
                        insideWhat = '\0';
                        inside = false;
                    }
                    escaped = false;
                    break;
                case '#':
                    if (!inside) {
                        // First comment symbol we encounter (not in a string) is the start of a comment
                        return i;
                    }
                    break;
                default:
                    escaped = false;
                    break;
            }
        }
        // Couldn't find a '#' that symbolized the start of a comma
        return -1;
    }
}
