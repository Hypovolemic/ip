package tasks;

public class Task {
    private String description;
    private boolean isDone;
    private char taskChar;

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
