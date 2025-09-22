package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
     * @throws IllegalArgumentException if the end time is before the start time.
     */
    public EventTask(String description, LocalDateTime from, LocalDateTime to) {
        super(description, TASK_TYPE);

        if (to.isBefore(from)) {
            throw new IllegalArgumentException("Event end time cannot be before start time");
        }

        this.from = from;
        this.to = to;
    }

    /**
     * Gets the start time of the event.
     *
     * @return The start time as LocalDateTime.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Gets the end time of the event.
     *
     * @return The end time as LocalDateTime.
     */
    public LocalDateTime getTo() {
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
        return super.toString() + " (from: " + formatFrom() + " to: " + formatTo() + ")";
    }
}
