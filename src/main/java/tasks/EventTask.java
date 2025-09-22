package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that needs to be completed by a specific deadline.
 */
public class EventTask extends Task {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy, HH:mm");
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Constructs an EventTask with the given description, start time, and end time.
     *
     * @param description The description of the task.
     * @param from The start time in "yyyy-MM-dd HHmm" format.
     * @param to The end time in "yyyy-MM-dd HHmm" format.
     */
    public EventTask(String description, LocalDateTime from, LocalDateTime to) {
        super(description, 'E');

        // Assertions after super() call
        assert description != null : "Event description should not be null";
        assert !description.trim().isEmpty() : "Event description should not be empty";
        assert from != null : "Event start time should not be null";
        assert to != null : "Event end time should not be null";
        assert !to.isBefore(from) : "Event end time should not be before start time";

        this.from = from;
        this.to = to;

        assert this.from != null : "Start time should be properly initialized";
        assert this.to != null : "End time should be properly initialized";
        assert getType() == 'E' : "EventTask should have type 'E'";
    }

    public LocalDateTime getFrom() {
        assert from != null : "From time should not be null";
        return from;
    }

    public LocalDateTime getTo() {
        assert to != null : "To time should not be null";
        return to;
    }

    @Override
    public String toString() {
        String result = super.toString() + " (from: " + this.from + " to: " + this.to + ")";
        assert result != null : "toString should not return null";
        assert result.contains(getDescription()) : "toString should contain task description";
        assert result.contains("from:") : "toString should contain start time indicator";
        assert result.contains("to:") : "toString should contain end time indicator";
        return result;
    }
}
