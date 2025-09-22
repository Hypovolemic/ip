package storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import tasks.DeadlineTask;
import tasks.EventTask;
import tasks.Task;
import tasks.TodoTask;

/**
 * Singleton class to manage loading and saving tasks to a text file.
 */
public class TasksStorage {
    private static final Path DATA_DIRECTORY = Paths.get("data");
    private static TasksStorage instance = null;
    private final Path tasksFilePath = Paths.get(DATA_DIRECTORY.toString(), "Tasks.txt");

    private TasksStorage() {
        try {
            if (Files.notExists(DATA_DIRECTORY)) {
                Files.createDirectories(DATA_DIRECTORY);
                System.out.println("Created data directory: " + DATA_DIRECTORY.toAbsolutePath());
            }
            if (Files.notExists(tasksFilePath)) {
                Files.createFile(tasksFilePath);
                System.out.println("Created tasks file: " + tasksFilePath.toAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error initializing storage: " + e.getMessage());
            System.err.println("Working directory: " + System.getProperty("user.dir"));
            throw new RuntimeException("Failed to initialize storage", e);
        }
    }

    /**
     * Gets the singleton instance of TasksStorage.
     * @return the TasksStorage instance
     */
    public static TasksStorage getInstance() {
        if (instance == null) {
            instance = new TasksStorage();
        }
        return instance;
    }

    /**
     * Loads tasks from the storage file.
     * @return list of loaded tasks
     */
    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            if (!Files.exists(tasksFilePath)) {
                System.out.println("Tasks file does not exist, starting with empty list");
                return tasks;
            }

            List<String> lines = Files.readAllLines(tasksFilePath);
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Task task = parseTaskFromLine(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    System.err.println("Skipping corrupted task line: " + line + " (Error: " + e.getMessage() + ")");
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
        return tasks;
    }

    private Task parseTaskFromLine(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Insufficient parts in task line");
        }

        char taskType = parts[0].charAt(0);
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (taskType) {
        case 'T':
            task = new TodoTask(description);
            break;
        case 'D':
            if (parts.length < 4) {
                throw new IllegalArgumentException("Deadline task missing 'by' field");
            }
            String by = parts[3];
            task = new DeadlineTask(description, by);
            break;
        case 'E':
            if (parts.length < 5) {
                throw new IllegalArgumentException("Event task missing from/to fields");
            }
            String from = parts[3];
            String to = parts[4];

            // Handle both old format (with T) and new format (with space)
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            DateTimeFormatter isoFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

            LocalDateTime fromDateTime, toDateTime;
            try {
                fromDateTime = LocalDateTime.parse(from, inputFormat);
                toDateTime = LocalDateTime.parse(to, inputFormat);
            } catch (DateTimeParseException e1) {
                try {
                    // Try ISO format as fallback
                    fromDateTime = LocalDateTime.parse(from, isoFormat);
                    toDateTime = LocalDateTime.parse(to, isoFormat);
                } catch (DateTimeParseException e2) {
                    throw new IllegalArgumentException("Invalid date format in event task");
                }
            }
            task = new EventTask(description, fromDateTime, toDateTime);
            break;
        default:
            throw new IllegalArgumentException("Unknown task type: " + taskType);
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Saves tasks to the storage file.
     * @param tasks the list of tasks to save
     */
    public void saveTasks(List<Task> tasks) {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            StringBuilder line = new StringBuilder();
            line.append(task instanceof TodoTask ? 'T' : task instanceof DeadlineTask ? 'D' : 'E');
            line.append(" | ");
            line.append(task.isDone() ? "1" : "0");
            line.append(" | ");
            line.append(task.getDescription());

            if (task instanceof DeadlineTask) {
                line.append(" | ").append(((DeadlineTask) task).formatBy());
            } else if (task instanceof EventTask) {
                EventTask eventTask = (EventTask) task;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
                line.append(" | ").append(eventTask.getFrom().format(formatter));
                line.append(" | ").append(eventTask.getTo().format(formatter));
            }

            lines.add(line.toString());
        }

        try {
            Files.write(tasksFilePath, lines);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }
}
