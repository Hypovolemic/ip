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
        this.by = parseDateTime(by);
    }

    /**
     * Parses the deadline string into a LocalDateTime object.
     *
     * @param dateTimeString The deadline in "yyyy-MM-dd HHmm" format.
     * @return The parsed LocalDateTime object.
     * @throws DateTimeParseException if the date-time string is not in the correct format.
     */
    private LocalDateTime parseDateTime(String dateTimeString) throws DateTimeParseException {
        return LocalDateTime.parse(dateTimeString, INPUT_FORMAT);
    }

    /**
     * Returns the deadline formatted as "dd MMM yyyy HHmm".
     *
     * @return The formatted deadline string.
     */
    public String formatBy() {
        return by.format(OUTPUT_FORMAT);
    }

    /**
     * Gets the deadline LocalDateTime.
     *
     * @return The deadline LocalDateTime object.
     */
    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + formatBy() + ")";
    }
}
