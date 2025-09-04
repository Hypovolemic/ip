package tasks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoTaskTest {
    @Test
    public void constructor_setsDescriptionCorrectly() {
        TodoTask task = new TodoTask("Buy milk");
        assertEquals("Buy milk", task.getDescription());
    }

    @Test
    public void constructor_setsTaskTypeToT() {
        TodoTask task = new TodoTask("Read book");
        assertEquals('T', task.getType());
    }
}