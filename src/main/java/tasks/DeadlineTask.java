package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that needs to be completed by a specific deadline.
 */
public class DeadlineTask extends Task {
    private LocalDateTime by;

    /**
     * Constructs a DeadlineTask with the given description and deadline.
     *
     * @param description The description of the task.
     * @param by The deadline in "yyyy-MM-dd HHmm" format.
     * @throws DateTimeParseException if the date-time string is not in the correct format.
     */
    public DeadlineTask(String description, String by) throws DateTimeParseException {
        super(description, 'D');
        this.by = parseDateTime(by);
    }

    private LocalDateTime parseDateTime(String by) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        return LocalDateTime.parse(by, formatter);
    }

    /**
     * Returns the deadline formatted as "dd MMM yyyy HHmm".
     *
     * @return The formatted deadline string.
     */
    public String formatBy() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HHmm");
        return by.format(formatter);
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + formatBy() + ")";
    }
}
