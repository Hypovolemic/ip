package tasks;

/**
 * Represents a to-do task without any date or time attached.
 */
public class TodoTask extends Task {
    public TodoTask(String description) {
        super(description, 'T');

        // Assertions after super() call
        assert description != null : "Todo description should not be null";
        assert !description.trim().isEmpty() : "Todo description should not be empty";
        assert getType() == 'T' : "TodoTask should have type 'T'";
        assert getDescription().equals(description) : "Description should be preserved";
    }
}
