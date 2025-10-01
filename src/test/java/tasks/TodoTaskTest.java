package tasks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import exceptions.FengWeiException;

/**
 * Test class for TodoTask functionality.
 * Tests the creation, validation, and behavior of TodoTask objects.
 */
public class TodoTaskTest {

    /**
     * Tests that the constructor correctly sets the task description.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void constructor_setsDescriptionCorrectly() throws FengWeiException {
        TodoTask task = new TodoTask("Buy milk");
        Assertions.assertEquals("Buy milk", task.getDescription());
    }

    /**
     * Tests that the constructor correctly sets the task type to 'T'.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void constructor_setsTaskTypeToT() throws FengWeiException {
        TodoTask task = new TodoTask("Read book");
        Assertions.assertEquals('T', task.getType());
    }

    /**
     * Tests that the constructor throws an exception when description is null.
     */
    @Test
    public void constructor_throwsExceptionOnNullDescription() {
        FengWeiException exception = Assertions.assertThrows(FengWeiException.class,
            () -> new TodoTask(null));
        Assertions.assertEquals("OOPS!!! The description of a todo cannot be null.", exception.getMessage());
    }

    /**
     * Tests that the constructor throws an exception when description is empty.
     */
    @Test
    public void constructor_throwsExceptionOnEmptyDescription() {
        FengWeiException exception = Assertions.assertThrows(FengWeiException.class,
            () -> new TodoTask(""));
        Assertions.assertEquals("OOPS!!! The description of a todo cannot be empty.", exception.getMessage());
    }

    /**
     * Tests that the constructor throws an exception when description contains only whitespace.
     */
    @Test
    public void constructor_throwsExceptionOnWhitespaceDescription() {
        FengWeiException exception = Assertions.assertThrows(FengWeiException.class,
            () -> new TodoTask("   "));
        Assertions.assertEquals("OOPS!!! The description of a todo cannot be empty.", exception.getMessage());
    }

    /**
     * Tests that a newly created task is initially not done.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void isDone_initiallyFalse() throws FengWeiException {
        TodoTask task = new TodoTask("Buy milk");
        Assertions.assertFalse(task.isDone());
    }

    /**
     * Tests that marking a task as done changes its status to true.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void markAsDone_changesStatusToTrue() throws FengWeiException {
        TodoTask task = new TodoTask("Buy milk");
        Assertions.assertFalse(task.isDone());

        task.markAsDone();
        Assertions.assertTrue(task.isDone());
    }

    /**
     * Tests that marking a completed task as not done changes its status to false.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void markAsNotDone_changesStatusToFalse() throws FengWeiException {
        TodoTask task = new TodoTask("Buy milk");
        task.markAsDone();
        Assertions.assertTrue(task.isDone());

        task.markAsNotDone();
        Assertions.assertFalse(task.isDone());
    }

    /**
     * Tests that the status icon returns a space when the task is not done.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void getStatusIcon_returnsSpaceWhenNotDone() throws FengWeiException {
        TodoTask task = new TodoTask("Buy milk");
        Assertions.assertEquals(" ", task.getStatusIcon());
    }

    /**
     * Tests that the status icon returns 'X' when the task is done.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void getStatusIcon_returnsXWhenDone() throws FengWeiException {
        TodoTask task = new TodoTask("Buy milk");
        task.markAsDone();
        Assertions.assertEquals("X", task.getStatusIcon());
    }

    /**
     * Tests that toString method contains all required elements for an incomplete task.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void toString_containsAllRequiredElements() throws FengWeiException {
        TodoTask task = new TodoTask("Buy milk");
        String result = task.toString();

        Assertions.assertTrue(result.contains("[T]"));
        Assertions.assertTrue(result.contains("[ ]"));
        Assertions.assertTrue(result.contains("Buy milk"));
        Assertions.assertEquals("[T][ ] Buy milk", result);
    }

    /**
     * Tests that toString method shows completed status for a done task.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void toString_showsCompletedStatus() throws FengWeiException {
        TodoTask task = new TodoTask("Buy milk");
        task.markAsDone();
        String result = task.toString();

        Assertions.assertTrue(result.contains("[T]"));
        Assertions.assertTrue(result.contains("[X]"));
        Assertions.assertTrue(result.contains("Buy milk"));
        Assertions.assertEquals("[T][X] Buy milk", result);
    }
}