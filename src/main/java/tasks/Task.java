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
        assert description != null : "Task description should not be null";
        assert !description.trim().isEmpty() : "Task description should not be empty";
        this.description = description;
        this.isDone = false;
        this.taskChar = taskChar;
        assert this.description != null : "Description should be properly initialized";
        assert !this.isDone : "New task should start as not done";
    }

    public String getStatusIcon() {
        String icon = (this.isDone ? "X" : " ");
        assert icon != null : "Status icon should not be null";
        assert icon.equals("X") || icon.equals(" ") : "Status icon should be either 'X' or ' '";
        return icon;
    }

    public String getDescription() {
        assert description != null : "Description should not be null";
        return description;
    }

    public char getType() {
        return taskChar;
    }

    @Override
    public String toString() {
        String result = "[" + this.taskChar + "][" + this.getStatusIcon() + "] " + this.description;
        assert result != null : "toString should not return null";
        assert result.contains(this.description) : "toString should contain the task description";
        return result;
    }

    public boolean isDone() {
        return isDone;
    }

    public void markAsDone() {
        boolean wasDone = this.isDone;
        this.isDone = true;
        assert this.isDone : "Task should be marked as done";
    }

    public void markAsNotDone() {
        boolean wasDone = this.isDone;
        this.isDone = false;
        assert !this.isDone : "Task should be marked as not done";
    }
}
