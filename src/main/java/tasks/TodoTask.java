package tasks;

/**
 * Represents a to-do task without any date or time attached.
 */
public class TodoTask extends Task {
    private static final char TASK_TYPE = 'T';

    /**
     * Constructs a TodoTask with the given description.
     *
     * @param description The description of the task.
     */
    public TodoTask(String description) {
        super(description, TASK_TYPE);
    }
}
