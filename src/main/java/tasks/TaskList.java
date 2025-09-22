package tasks;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a list of tasks and provides operations to modify the list.
 */
public class TaskList {
    private final List<Task> tasks;

    public TaskList(List<Task> tasks) {
        assert tasks != null : "Input task list should not be null";
        this.tasks = new ArrayList<>(tasks);
        assert this.tasks != null : "Internal task list should be initialized";
    }

    public TaskList() {
        this.tasks = new ArrayList<>();
        assert this.tasks != null : "Internal task list should be initialized";
    }

    /**
     * Finds and returns a list of tasks that contain the specified keyword in their description.
     *
     * @param keyword The keyword to search for within task descriptions.
     * @return A list of tasks that match the keyword.
     */
    public List<Task> findTasks(String keyword) {
        assert keyword != null : "Keyword should not be null";
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            assert task != null : "Task in list should not be null";
            assert task.getDescription() != null : "Task description should not be null";
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.add(task);
            }
        }
        assert matchingTasks != null : "Matching tasks list should not be null";
        return matchingTasks;
    }

    public void add(Task task) {
        assert task != null : "Task to add should not be null";
        int initialSize = tasks.size();
        tasks.add(task);
        assert tasks.size() == initialSize + 1 : "Task list size should increase by 1 after adding";
        assert tasks.contains(task) : "Added task should be in the list";
    }

    public Task remove(int index) {
        assert index >= 0 && index < tasks.size() : "Index should be within valid range";
        int initialSize = tasks.size();
        Task removedTask = tasks.remove(index);
        assert removedTask != null : "Removed task should not be null";
        assert tasks.size() == initialSize - 1 : "Task list size should decrease by 1 after removal";
        return removedTask;
    }

    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "Index should be within valid range";
        Task task = tasks.get(index);
        assert task != null : "Retrieved task should not be null";
        return task;
    }

    public int size() {
        int size = tasks.size();
        assert size >= 0 : "Size should never be negative";
        return size;
    }

    public List<Task> getAll() {
        List<Task> copy = new ArrayList<>(tasks);
        assert copy != null : "Copy of task list should not be null";
        assert copy.size() == tasks.size() : "Copy should have same size as original";
        return copy;
    }

    public void markAsDone(int index) {
        assert index >= 0 && index < tasks.size() : "Index should be within valid range";
        Task task = tasks.get(index);
        assert task != null : "Task to mark should not be null";
        boolean wasNotDone = !task.isDone();
        task.markAsDone();
        assert task.isDone() : "Task should be marked as done after calling markAsDone";
    }

    public void markAsNotDone(int index) {
        assert index >= 0 && index < tasks.size() : "Index should be within valid range";
        Task task = tasks.get(index);
        assert task != null : "Task to unmark should not be null";
        boolean wasDone = task.isDone();
        task.markAsNotDone();
        assert !task.isDone() : "Task should be marked as not done after calling markAsNotDone";
    }
}
