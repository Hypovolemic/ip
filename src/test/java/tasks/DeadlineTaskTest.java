import tasks.DeadlineTask;

import org.junit.jupiter.api.Test;
import java.time.format.DateTimeParseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeadlineTaskTest {
    @Test
    public void constructor_setsDescriptionAndTypeCorrectly() {
        DeadlineTask task = new DeadlineTask("Submit report", "2024-06-30 2359");
        assertEquals("Submit report", task.getDescription());
        assertEquals('D', task.getType());
    }

    @Test
    public void formatBy_formatsDateCorrectly() {
        DeadlineTask task = new DeadlineTask("Submit report", "2024-06-30 2359");
        assertEquals("30 Jun 2024 2359", task.formatBy());
    }



    @Test
    public void constructor_throwsExceptionOnInvalidDate() {
        assertThrows(DateTimeParseException.class, () -> {
            new DeadlineTask("Submit report", "invalid-date");
        });
    }
}
