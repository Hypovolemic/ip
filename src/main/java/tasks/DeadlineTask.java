package tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DeadlineTask extends Task {
    private LocalDateTime by;

    public DeadlineTask(String description, String by) throws DateTimeParseException {
        super(description, 'D');
        this.by = parseDateTime(by);
    }

    private LocalDateTime parseDateTime(String by) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        return LocalDateTime.parse(by, formatter);
    }

    public String formatBy() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HHmm");
        return by.format(formatter);
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + formatBy() + ")";
    }
}
