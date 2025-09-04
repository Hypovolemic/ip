package tasks;

/**
 * Represents a to-do task without any date or time attached.
 */
public class TodoTask extends Task {
    public TodoTask(String description) {
        super(description, 'T');
    }
}
