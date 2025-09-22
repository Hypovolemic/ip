package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import exceptions.FengWeiException;

/**
 * Represents a task that occurs during a specific time period.
 */
public class EventTask extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy, HH:mm");
    private static final char TASK_TYPE = 'E';

    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Constructs an EventTask with the given description, start time, and end time.
     *
     * @param description The description of the task.
     * @param from The start time of the event.
     * @param to The end time of the event.
     * @throws FengWeiException if the description is null/empty or if the end time is before the start time.
     */
    public EventTask(String description, LocalDateTime from, LocalDateTime to) throws FengWeiException {
        super(validateDescription(description), TASK_TYPE);

        // Assertions after super() call - from Assertions branch
        assert description != null : "Event description should not be null";
        assert !description.trim().isEmpty() : "Event description should not be empty";
        assert from != null : "Event start time should not be null";
        assert to != null : "Event end time should not be null";

        validateTimes(from, to);

        this.from = from;
        this.to = to;

        assert this.from != null : "Start time should be properly initialized";
        assert this.to != null : "End time should be properly initialized";
        assert getType() == TASK_TYPE : "EventTask should have type 'E'";
    }

    /**
     * Validates the task description.
     *
     * @param description the description to validate
     * @return the validated description
     * @throws FengWeiException if the description is invalid
     */
    private static String validateDescription(String description) throws FengWeiException {
        if (description == null) {
            throw new FengWeiException("OOPS!!! The description of an event cannot be null.");
        }
        if (description.trim().isEmpty()) {
            throw new FengWeiException("OOPS!!! The description of an event cannot be empty.");
        }
        return description;
    }

    /**
     * Validates the event times.
     *
     * @param from the start time
     * @param to the end time
     * @throws FengWeiException if the times are invalid
     */
    private static void validateTimes(LocalDateTime from, LocalDateTime to) throws FengWeiException {
        if (from == null) {
            throw new FengWeiException("OOPS!!! The event start time cannot be null.");
        }
        if (to == null) {
            throw new FengWeiException("OOPS!!! The event end time cannot be null.");
        }
        if (to.isBefore(from)) {
            throw new FengWeiException("OOPS!!! Event end time cannot be before start time.");
        }
    }

    /**
     * Gets the start time of the event.
     *
     * @return The start time as LocalDateTime.
     */
    public LocalDateTime getFrom() {
        assert from != null : "From time should not be null";
        return from;
    }

    /**
     * Gets the end time of the event.
     *
     * @return The end time as LocalDateTime.
     */
    public LocalDateTime getTo() {
        assert to != null : "To time should not be null";
        return to;
    }

    /**
     * Formats the start time for display.
     *
     * @return The formatted start time string.
     */
    public String formatFrom() {
        return from.format(OUTPUT_FORMAT);
    }

    /**
     * Formats the end time for display.
     *
     * @return The formatted end time string.
     */
    public String formatTo() {
        return to.format(OUTPUT_FORMAT);
    }

    @Override
    public String toString() {
        String result = super.toString() + " (from: " + formatFrom() + " to: " + formatTo() + ")";
        assert result != null : "toString should not return null";
        assert result.contains(getDescription()) : "toString should contain task description";
        assert result.contains("from:") : "toString should contain start time indicator";
        assert result.contains("to:") : "toString should contain end time indicator";
        return result;
    }
}
