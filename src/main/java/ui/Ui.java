package ui;

import java.util.List;
import java.util.Scanner;

import tasks.Task;

/**
 * Handles user interface interactions including input/output operations.
 */
public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Displays the welcome message when the application starts.
     */
    public void showWelcome() {
        System.out.println("_____________________________________________________");
        System.out.println("Hello! I'm FengWei");
        System.out.println("What can I do for you?");
        System.out.println("_____________________________________________________");
    }

    /**
     * Gets the welcome message as a string for GUI display.
     * @return the welcome message string
     */
    public String getWelcomeMessage() {
        return "Hello! I'm FengWei\nWhat can I do for you?";
    }

    /**
     * Displays a separator line.
     */
    public void showLine() {
        System.out.println("_____________________________________________________");
    }

    /**
     * Reads a command from user input.
     * @return the user input string
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays an error message.
     * @param message the error message to display
     */
    public void showError(String message) {
        showLine();
        System.out.println(" OOPS!!! " + message);
        showLine();
    }

    /**
     * Displays a loading error message.
     */
    public void showLoadingError() {
        showLine();
        System.out.println(" OOPS!!! Error loading tasks from file.");
        showLine();
    }

    /**
     * Displays the goodbye message when the application exits.
     */
    public void showBye() {
        showLine();
        System.out.println("Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Displays the list of tasks.
     * @param tasks the list of tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        showLine();
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        showLine();
    }

    /**
     * Displays the found tasks from a search.
     * @param foundTasks the list of found tasks to display
     */
    public void showFoundTasks(List<Task> foundTasks) {
        showLine();
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < foundTasks.size(); i++) {
            System.out.println((i + 1) + "." + foundTasks.get(i));
        }
        showLine();
    }

    /**
     * Displays a confirmation message when a task is added.
     * @param task the task that was added
     * @param totalTasks the total number of tasks after adding
     */
    public void showTaskAdded(Task task, int totalTasks) {
        showLine();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
        showLine();
    }

    /**
     * Displays a confirmation message when a task is marked as done.
     * @param task the task that was marked
     */
    public void showTaskMarked(Task task) {
        showLine();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("    " + task);
        showLine();
    }

    /**
     * Displays a confirmation message when a task is unmarked.
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        showLine();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("    " + task);
        showLine();
    }

    /**
     * Displays a confirmation message when a task is deleted.
     * @param task the task that was deleted
     * @param remainingTasks the number of remaining tasks after deletion
     */
    public void showTaskDeleted(Task task, int remainingTasks) {
        showLine();
        System.out.println("Noted. I've removed this task:");
        System.out.println(" " + task);
        System.out.println("Now you have " + remainingTasks + " tasks in the list.");
        showLine();
    }

    /**
     * Displays the help information showing available commands.
     */
    public void showHelp() {
        showLine();
        System.out.println("Here are the available commands:");
        System.out.println("1. list - Shows the list of tasks.");
        System.out.println("2. find <keyword> - Finds tasks matching the keyword.");
        System.out.println("3. todo <description> - Adds a todo task.");
        System.out.println("4. deadline <description> /by <time> - Adds a deadline task.");
        System.out.println("   Time format: YYYY-MM-DD HHMM (e.g., 2025-12-25 1400)");
        System.out.println("5. event <description> /from <start> /to <end> - Adds an event task.");
        System.out.println("   Time format: YYYY-MM-DD HHMM (e.g., 2025-12-25 1400)");
        System.out.println("6. mark <task number> - Marks a task as done.");
        System.out.println("7. unmark <task number> - Marks a task as not done.");
        System.out.println("8. delete <task number> - Deletes a task from the list.");
        System.out.println("9. help - Shows this help message.");
        System.out.println("10. bye - Exits the program.");
        showLine();
    }
}
