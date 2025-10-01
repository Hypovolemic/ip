package tasks;

import exceptions.FengWeiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.time.LocalDateTime;

/**
 * Test class for EventTask functionality.
 * Tests the creation, validation, time formatting, and behavior of EventTask objects.
 */
public class EventTaskTest {

    /**
     * Tests that the constructor correctly sets the description and task type.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void constructor_setsDescriptionAndTypeCorrectly() throws FengWeiException {
        LocalDateTime from = LocalDateTime.of(2024, 6, 30, 14, 0);
        LocalDateTime to = LocalDateTime.of(2024, 6, 30, 16, 0);
        EventTask task = new EventTask("Team meeting", from, to);

        Assertions.assertEquals("Team meeting", task.getDescription());
        Assertions.assertEquals('E', task.getType());
    }

    /**
     * Tests that the constructor correctly sets the start and end times.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void constructor_setsTimesCorrectly() throws FengWeiException {
        LocalDateTime from = LocalDateTime.of(2024, 6, 30, 14, 0);
        LocalDateTime to = LocalDateTime.of(2024, 6, 30, 16, 0);
        EventTask task = new EventTask("Team meeting", from, to);

        Assertions.assertEquals(from, task.getFrom());
        Assertions.assertEquals(to, task.getTo());
    }

    /**
     * Tests that the constructor throws an exception when description is null.
     */
    @Test
    public void constructor_throwsExceptionOnNullDescription() {
        LocalDateTime from = LocalDateTime.of(2024, 6, 30, 14, 0);
        LocalDateTime to = LocalDateTime.of(2024, 6, 30, 16, 0);

        FengWeiException exception = Assertions.assertThrows(FengWeiException.class,
            () -> new EventTask(null, from, to));
        Assertions.assertEquals("OOPS!!! The description of an event cannot be null.", exception.getMessage());
    }

    /**
     * Tests that the constructor throws an exception when description is empty.
     */
    @Test
    public void constructor_throwsExceptionOnEmptyDescription() {
        LocalDateTime from = LocalDateTime.of(2024, 6, 30, 14, 0);
        LocalDateTime to = LocalDateTime.of(2024, 6, 30, 16, 0);

        FengWeiException exception = Assertions.assertThrows(FengWeiException.class,
            () -> new EventTask("", from, to));
        Assertions.assertEquals("OOPS!!! The description of an event cannot be empty.", exception.getMessage());
    }

    /**
     * Tests that the constructor throws an exception when description contains only whitespace.
     */
    @Test
    public void constructor_throwsExceptionOnWhitespaceDescription() {
        LocalDateTime from = LocalDateTime.of(2024, 6, 30, 14, 0);
        LocalDateTime to = LocalDateTime.of(2024, 6, 30, 16, 0);

        FengWeiException exception = Assertions.assertThrows(FengWeiException.class,
            () -> new EventTask("   ", from, to));
        Assertions.assertEquals("OOPS!!! The description of an event cannot be empty.", exception.getMessage());
    }

    /**
     * Tests that the constructor throws an exception when end time is before start time.
     */
    @Test
    public void constructor_throwsExceptionWhenEndTimeBeforeStartTime() {
        LocalDateTime from = LocalDateTime.of(2024, 6, 30, 16, 0);
        LocalDateTime to = LocalDateTime.of(2024, 6, 30, 14, 0);

        FengWeiException exception = Assertions.assertThrows(FengWeiException.class,
            () -> new EventTask("Meeting", from, to));
        Assertions.assertEquals("OOPS!!! Event end time cannot be before start time.", exception.getMessage());
    }

    /**
     * Tests that the formatFrom method correctly formats the start time.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void formatFrom_formatsTimeCorrectly() throws FengWeiException {
        LocalDateTime from = LocalDateTime.of(2024, 6, 30, 14, 30);
        LocalDateTime to = LocalDateTime.of(2024, 6, 30, 16, 0);
        EventTask task = new EventTask("Team meeting", from, to);

        Assertions.assertEquals("Jun 30 2024, 14:30", task.formatFrom());
    }

    /**
     * Tests that the formatTo method correctly formats the end time.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void formatTo_formatsTimeCorrectly() throws FengWeiException {
        LocalDateTime from = LocalDateTime.of(2024, 6, 30, 14, 0);
        LocalDateTime to = LocalDateTime.of(2024, 6, 30, 16, 15);
        EventTask task = new EventTask("Team meeting", from, to);

        Assertions.assertEquals("Jun 30 2024, 16:15", task.formatTo());
    }

    /**
     * Tests that toString method contains all required elements including event times.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void toString_containsAllRequiredElements() throws FengWeiException {
        LocalDateTime from = LocalDateTime.of(2024, 6, 30, 14, 0);
        LocalDateTime to = LocalDateTime.of(2024, 6, 30, 16, 0);
        EventTask task = new EventTask("Team meeting", from, to);

        String result = task.toString();
        Assertions.assertTrue(result.contains("Team meeting"));
        Assertions.assertTrue(result.contains("from:"));
        Assertions.assertTrue(result.contains("to:"));
        Assertions.assertTrue(result.contains("Jun 30 2024, 14:00"));
        Assertions.assertTrue(result.contains("Jun 30 2024, 16:00"));
    }

    /**
     * Tests that the constructor accepts events with the same start and end time.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void constructor_acceptsSameStartAndEndTime() throws FengWeiException {
        LocalDateTime time = LocalDateTime.of(2024, 6, 30, 14, 0);
        EventTask task = new EventTask("Quick meeting", time, time);

        Assertions.assertEquals(time, task.getFrom());
        Assertions.assertEquals(time, task.getTo());
    }
}
