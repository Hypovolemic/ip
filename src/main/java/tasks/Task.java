package tasks;

/**
 * Represents a generic task with a description and type.
 * Serves as the superclass for specific task types.
 */
public class Task {
    private final String description;
    private boolean isDone;
    private final char taskChar;

    /**
     * Constructor for a Task object.
     * @param description The description of the task.
     * @param taskChar    A character representing the type of task
     *                    (e.g., 'T' for Todo, 'D' for Deadline, 'E' for Event).
     */
    public Task(String description, char taskChar) {
        this.description = description;
        this.isDone = false;
        this.taskChar = taskChar;
    }

    public String getStatusIcon() {
        return (this.isDone ? "X" : " "); // mark done task with X
    }

    public String getDescription() {
        return description;
    }

    public char getType() {
        return taskChar;
    }

    // removed getDescription method and implemented toString method
    @Override
    public String toString() {
        return "[" + this.taskChar + "][" + this.getStatusIcon() + "] " + this.description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }
}
