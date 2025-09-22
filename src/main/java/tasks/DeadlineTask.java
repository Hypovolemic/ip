package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that needs to be completed by a specific deadline.
 */
public class DeadlineTask extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy HHmm");
    private static final char TASK_TYPE = 'D';

    private final LocalDateTime by;

    /**
     * Constructs a DeadlineTask with the given description and deadline.
     *
     * @param description The description of the task.
     * @param by The deadline in "yyyy-MM-dd HHmm" format.
     * @throws DateTimeParseException if the date-time string is not in the correct format.
     */
    public DeadlineTask(String description, String by) throws DateTimeParseException {
        super(description, TASK_TYPE);

        // Assertions after super() call
        assert description != null : "Deadline description should not be null";
        assert !description.trim().isEmpty() : "Deadline description should not be empty";
        assert by != null : "Deadline time string should not be null";
        assert !by.trim().isEmpty() : "Deadline time string should not be empty";

        this.by = parseDateTime(by);

        assert this.by != null : "Parsed deadline should not be null";
        assert getType() == TASK_TYPE : "DeadlineTask should have type 'D'";
    }

    /**
     * Parses the deadline string into a LocalDateTime object.
     *
     * @param dateTimeString The deadline in "yyyy-MM-dd HHmm" format.
     * @return The parsed LocalDateTime object.
     * @throws DateTimeParseException if the date-time string is not in the correct format.
     */
    private LocalDateTime parseDateTime(String dateTimeString) throws DateTimeParseException {
        assert dateTimeString != null : "Input date string should not be null";
        LocalDateTime result = LocalDateTime.parse(dateTimeString, INPUT_FORMAT);
        assert result != null : "Parsed LocalDateTime should not be null";
        return result;
    }

    /**
     * Returns the deadline formatted as "dd MMM yyyy HHmm".
     *
     * @return The formatted deadline string.
     */
    public String formatBy() {
        assert by != null : "Deadline should be set before formatting";
        String formatted = by.format(OUTPUT_FORMAT);
        assert formatted != null : "Formatted date should not be null";
        assert !formatted.isEmpty() : "Formatted date should not be empty";
        return formatted;
    }

    /**
     * Gets the deadline LocalDateTime.
     *
     * @return The deadline LocalDateTime object.
     */
    public LocalDateTime getBy() {
        assert by != null : "Deadline should not be null";
        return by;
    }

    @Override
    public String toString() {
        String result = super.toString() + " (by: " + formatBy() + ")";
        assert result != null : "toString should not return null";
        assert result.contains(getDescription()) : "toString should contain task description";
        assert result.contains("by:") : "toString should contain deadline indicator";
        return result;
    }
}
