package tasks;

import exceptions.FengWeiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.time.format.DateTimeParseException;

/**
 * Test class for DeadlineTask functionality.
 * Tests the creation, validation, date formatting, and behavior of DeadlineTask objects.
 */
public class DeadlineTaskTest {

    /**
     * Tests that the constructor correctly sets the description and task type.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void constructor_setsDescriptionAndTypeCorrectly() throws FengWeiException {
        DeadlineTask task = new DeadlineTask("Submit report", "2024-06-30 2359");
        Assertions.assertEquals("Submit report", task.getDescription());
        Assertions.assertEquals('D', task.getType());
    }

    /**
     * Tests that the formatBy method correctly formats the deadline date.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void formatBy_formatsDateCorrectly() throws FengWeiException {
        DeadlineTask task = new DeadlineTask("Submit report", "2024-06-30 2359");
        Assertions.assertEquals("30 Jun 2024 2359", task.formatBy());
    }

    /**
     * Tests that the constructor throws DateTimeParseException for invalid date formats.
     */
    @Test
    public void constructor_throwsExceptionOnInvalidDate() {
        Assertions.assertThrows(DateTimeParseException.class, () -> new DeadlineTask("Submit report", "invalid-date"));
    }

    /**
     * Tests that the constructor throws an exception when description is null.
     */
    @Test
    public void constructor_throwsExceptionOnNullDescription() {
        FengWeiException exception = Assertions.assertThrows(FengWeiException.class,
            () -> new DeadlineTask(null, "2024-06-30 2359"));
        Assertions.assertEquals("OOPS!!! The description of a deadline cannot be null.", exception.getMessage());
    }

    /**
     * Tests that the constructor throws an exception when description is empty.
     */
    @Test
    public void constructor_throwsExceptionOnEmptyDescription() {
        FengWeiException exception = Assertions.assertThrows(FengWeiException.class,
            () -> new DeadlineTask("", "2024-06-30 2359"));
        Assertions.assertEquals("OOPS!!! The description of a deadline cannot be empty.", exception.getMessage());
    }

    /**
     * Tests that the constructor throws an exception when description contains only whitespace.
     */
    @Test
    public void constructor_throwsExceptionOnWhitespaceDescription() {
        FengWeiException exception = Assertions.assertThrows(FengWeiException.class,
            () -> new DeadlineTask("   ", "2024-06-30 2359"));
        Assertions.assertEquals("OOPS!!! The description of a deadline cannot be empty.", exception.getMessage());
    }

    /**
     * Tests that a newly created deadline task is initially not done.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void isDone_initiallyFalse() throws FengWeiException {
        DeadlineTask task = new DeadlineTask("Submit report", "2024-06-30 2359");
        Assertions.assertFalse(task.isDone());
    }

    /**
     * Tests that marking a deadline task as done changes its status to true.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void markAsDone_changesStatusToTrue() throws FengWeiException {
        DeadlineTask task = new DeadlineTask("Submit report", "2024-06-30 2359");
        Assertions.assertFalse(task.isDone());

        task.markAsDone();
        Assertions.assertTrue(task.isDone());
    }

    /**
     * Tests that marking a completed deadline task as not done changes its status to false.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void markAsNotDone_changesStatusToFalse() throws FengWeiException {
        DeadlineTask task = new DeadlineTask("Submit report", "2024-06-30 2359");
        task.markAsDone();
        Assertions.assertTrue(task.isDone());

        task.markAsNotDone();
        Assertions.assertFalse(task.isDone());
    }

    /**
     * Tests that the status icon returns a space when the deadline task is not done.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void getStatusIcon_returnsSpaceWhenNotDone() throws FengWeiException {
        DeadlineTask task = new DeadlineTask("Submit report", "2024-06-30 2359");
        Assertions.assertEquals(" ", task.getStatusIcon());
    }

    /**
     * Tests that the status icon returns 'X' when the deadline task is done.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void getStatusIcon_returnsXWhenDone() throws FengWeiException {
        DeadlineTask task = new DeadlineTask("Submit report", "2024-06-30 2359");
        task.markAsDone();
        Assertions.assertEquals("X", task.getStatusIcon());
    }

    /**
     * Tests that toString method contains all required elements including deadline information.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void toString_containsAllRequiredElements() throws FengWeiException {
        DeadlineTask task = new DeadlineTask("Submit report", "2024-06-30 2359");
        String result = task.toString();

        Assertions.assertTrue(result.contains("[D]"));
        Assertions.assertTrue(result.contains("[ ]"));
        Assertions.assertTrue(result.contains("Submit report"));
        Assertions.assertTrue(result.contains("30 Jun 2024 2359"));
        Assertions.assertTrue(result.contains("(by:"));
    }

    /**
     * Tests that toString method shows completed status for a done deadline task.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void toString_showsCompletedStatus() throws FengWeiException {
        DeadlineTask task = new DeadlineTask("Submit report", "2024-06-30 2359");
        task.markAsDone();
        String result = task.toString();

        Assertions.assertTrue(result.contains("[D]"));
        Assertions.assertTrue(result.contains("[X]"));
        Assertions.assertTrue(result.contains("Submit report"));
        Assertions.assertTrue(result.contains("30 Jun 2024 2359"));
    }

    /**
     * Tests that the constructor accepts various valid date-time formats.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void constructor_acceptsValidDateTimeFormats() throws FengWeiException {
        // Test various valid date-time formats
        DeadlineTask task1 = new DeadlineTask("Task 1", "2024-01-01 0000");
        Assertions.assertEquals("Task 1", task1.getDescription());

        DeadlineTask task2 = new DeadlineTask("Task 2", "2024-12-25 1630");
        Assertions.assertEquals("Task 2", task2.getDescription());
    }
}
