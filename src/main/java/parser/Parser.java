package parser;

/**
 * Utility class for parsing user input commands and their arguments.
 */
public class Parser {
    public static String getCommand(String input) {
        String[] parts = input.trim().split(" ", 2);
        return parts[0];
    }

    public static String getArguments(String input) {
        String[] parts = input.trim().split(" ", 2);
        return parts.length > 1 ? parts[1] : "";
    }
}
