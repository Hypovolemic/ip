package tasks;

import exceptions.FengWeiException;

/**
 * Represents a to-do task without any date or time attached.
 */
public class TodoTask extends Task {
    private static final char TASK_TYPE = 'T';

    /**
     * Constructs a TodoTask with the given description.
     *
     * @param description The description of the task.
     * @throws FengWeiException if the description is null or empty
     */
    public TodoTask(String description) throws FengWeiException {
        super(validateDescription(description), TASK_TYPE);

        // Assertions after super() call - from Assertions branch
        assert description != null : "Todo description should not be null";
        assert !description.trim().isEmpty() : "Todo description should not be empty";
        assert getType() == TASK_TYPE : "TodoTask should have type 'T'";
        assert getDescription().equals(description) : "Description should be preserved";
    }

    /**
     * Validates the todo description and throws appropriate exceptions for invalid input.
     *
     * @param description the description to validate
     * @return the validated description
     * @throws FengWeiException if the description is invalid
     */
    private static String validateDescription(String description) throws FengWeiException {
        if (description == null) {
            throw new FengWeiException("OOPS!!! The description of a todo cannot be null.");
        }
        if (description.trim().isEmpty()) {
            throw new FengWeiException("OOPS!!! The description of a todo cannot be empty.");
        }
        return description;
    }
}
