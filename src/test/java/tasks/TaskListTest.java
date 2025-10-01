package tasks;

import exceptions.FengWeiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for TaskList functionality.
 * Tests task list operations including adding, removing, marking tasks, and search functionality.
 * Uses setup method to initialize common test data for consistent testing.
 */
public class TaskListTest {
    /** Task list instance used for testing. */
    private TaskList taskList;
    /** Sample todo task for testing. */
    private TodoTask todoTask;
    /** Sample deadline task for testing. */
    private DeadlineTask deadlineTask;

    /**
     * Sets up test fixtures before each test method.
     * Initializes a new TaskList and creates sample tasks for testing.
     *
     * @throws FengWeiException if task creation fails
     */
    @BeforeEach
    public void setUp() throws FengWeiException {
        taskList = new TaskList();
        todoTask = new TodoTask("Buy groceries");
        deadlineTask = new DeadlineTask("Submit report", "2024-12-31 2359");
    }

    /**
     * Tests that the default constructor creates an empty task list.
     */
    @Test
    public void constructor_createsEmptyList() {
        TaskList list = new TaskList();
        Assertions.assertEquals(0, list.size());
    }

    /**
     * Tests that the constructor with existing list parameter correctly initializes the task list.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void constructor_withExistingList() throws FengWeiException {
        List<Task> initialTasks = new ArrayList<>();
        initialTasks.add(todoTask);
        initialTasks.add(deadlineTask);

        TaskList list = new TaskList(initialTasks);
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals(todoTask.getDescription(), list.get(0).getDescription());
        Assertions.assertEquals(deadlineTask.getDescription(), list.get(1).getDescription());
    }

    /**
     * Tests that adding tasks increases the list size correctly.
     */
    @Test
    public void add_increasesSize() {
        Assertions.assertEquals(0, taskList.size());
        taskList.add(todoTask);
        Assertions.assertEquals(1, taskList.size());
        taskList.add(deadlineTask);
        Assertions.assertEquals(2, taskList.size());
    }

    /**
     * Tests that added tasks are stored correctly and can be retrieved.
     */
    @Test
    public void add_storesTaskCorrectly() {
        taskList.add(todoTask);
        Assertions.assertEquals(todoTask, taskList.get(0));
    }

    /**
     * Tests that removing tasks decreases the list size correctly.
     */
    @Test
    public void remove_decreasesSize() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);
        Assertions.assertEquals(2, taskList.size());

        taskList.remove(0);
        Assertions.assertEquals(1, taskList.size());
    }

    /**
     * Tests that remove operation returns the correct task that was removed.
     */
    @Test
    public void remove_returnsCorrectTask() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);

        Task removed = taskList.remove(0);
        Assertions.assertEquals(todoTask, removed);
    }

    /**
     * Tests that get operation returns the correct task at the specified index.
     */
    @Test
    public void get_returnsCorrectTask() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);

        Assertions.assertEquals(todoTask, taskList.get(0));
        Assertions.assertEquals(deadlineTask, taskList.get(1));
    }

    /**
     * Tests that size method returns the correct number of tasks in the list.
     */
    @Test
    public void size_returnsCorrectSize() {
        Assertions.assertEquals(0, taskList.size());
        taskList.add(todoTask);
        Assertions.assertEquals(1, taskList.size());
        taskList.add(deadlineTask);
        Assertions.assertEquals(2, taskList.size());
        taskList.remove(0);
        Assertions.assertEquals(1, taskList.size());
    }

    /**
     * Tests that getAll method returns all tasks in the list.
     */
    @Test
    public void getAll_returnsAllTasks() {
        taskList.add(todoTask);
        taskList.add(deadlineTask);

        List<Task> allTasks = taskList.getAll();
        Assertions.assertEquals(2, allTasks.size());
        Assertions.assertEquals(todoTask, allTasks.get(0));
        Assertions.assertEquals(deadlineTask, allTasks.get(1));
    }

    /**
     * Tests that getAll method returns an independent copy that doesn't affect the original list.
     */
    @Test
    public void getAll_returnsIndependentCopy() {
        taskList.add(todoTask);
        List<Task> copy = taskList.getAll();

        // Modifying the copy should not affect original
        copy.clear();
        Assertions.assertEquals(1, taskList.size());
    }

    /**
     * Tests that markAsDone correctly marks a task as completed.
     */
    @Test
    public void markAsDone_marksTaskCorrectly() {
        taskList.add(todoTask);
        Assertions.assertFalse(todoTask.isDone());

        taskList.markAsDone(0);
        Assertions.assertTrue(todoTask.isDone());
    }

    /**
     * Tests that markAsNotDone correctly unmarks a completed task.
     *
     * @throws FengWeiException if task operations fail
     */
    @Test
    public void markAsNotDone_unmarksTaskCorrectly() throws FengWeiException {
        taskList.add(todoTask);
        todoTask.markAsDone(); // First mark as done
        Assertions.assertTrue(todoTask.isDone());

        taskList.markAsNotDone(0);
        Assertions.assertFalse(todoTask.isDone());
    }

    /**
     * Tests that findTasks method correctly finds tasks matching a keyword.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void findTasks_findsMatchingTasks() throws FengWeiException {
        TodoTask task1 = new TodoTask("Buy milk");
        TodoTask task2 = new TodoTask("Buy bread");
        TodoTask task3 = new TodoTask("Read book");

        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);

        List<Task> foundTasks = taskList.findTasks("buy");
        Assertions.assertEquals(2, foundTasks.size());
        Assertions.assertTrue(foundTasks.contains(task1));
        Assertions.assertTrue(foundTasks.contains(task2));
        Assertions.assertFalse(foundTasks.contains(task3));
    }

    /**
     * Tests that findTasks performs case-insensitive search.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void findTasks_caseInsensitiveSearch() throws FengWeiException {
        TodoTask task = new TodoTask("Buy MILK");
        taskList.add(task);

        List<Task> foundTasks = taskList.findTasks("milk");
        Assertions.assertEquals(1, foundTasks.size());
        Assertions.assertEquals(task, foundTasks.get(0));
    }

    /**
     * Tests that findTasks returns empty list when no tasks match the keyword.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void findTasks_returnsEmptyListWhenNoMatches() throws FengWeiException {
        TodoTask task = new TodoTask("Buy milk");
        taskList.add(task);

        List<Task> foundTasks = taskList.findTasks("bread");
        Assertions.assertEquals(0, foundTasks.size());
    }

    /**
     * Tests that findTasks correctly finds partial matches within task descriptions.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void findTasks_findsPartialMatches() throws FengWeiException {
        TodoTask task = new TodoTask("Programming assignment");
        taskList.add(task);

        List<Task> foundTasks = taskList.findTasks("program");
        Assertions.assertEquals(1, foundTasks.size());
        Assertions.assertEquals(task, foundTasks.get(0));
    }

    /**
     * Tests multiple operations working together correctly in a complex scenario.
     * This integration test verifies that add, mark, and remove operations work properly in sequence.
     *
     * @throws FengWeiException if task creation fails
     */
    @Test
    public void multipleOperations_workCorrectly() throws FengWeiException {
        // Add multiple tasks
        TodoTask task1 = new TodoTask("Task 1");
        TodoTask task2 = new TodoTask("Task 2");
        TodoTask task3 = new TodoTask("Task 3");

        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);
        Assertions.assertEquals(3, taskList.size());

        // Mark some as done
        taskList.markAsDone(0);
        taskList.markAsDone(2);
        Assertions.assertTrue(task1.isDone());
        Assertions.assertFalse(task2.isDone());
        Assertions.assertTrue(task3.isDone());

        // Remove middle task
        Task removed = taskList.remove(1);
        Assertions.assertEquals(task2, removed);
        Assertions.assertEquals(2, taskList.size());

        // Verify remaining tasks
        Assertions.assertEquals(task1, taskList.get(0));
        Assertions.assertEquals(task3, taskList.get(1));
    }
}
