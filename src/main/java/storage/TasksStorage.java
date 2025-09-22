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

import exceptions.FengWeiException;
import tasks.DeadlineTask;
import tasks.EventTask;
import tasks.Task;
import tasks.TodoTask;

/**
 * Singleton class to manage loading and saving tasks to a text file.
 */
public class TasksStorage {
    // File and directory constants
    private static final String DATA_DIRECTORY_NAME = "data";
    private static final String TASKS_FILE_NAME = "Tasks.txt";
    private static final Path DATA_DIRECTORY = Paths.get(DATA_DIRECTORY_NAME);

    // Task parsing constants
    private static final String TASK_DELIMITER = " \\| ";
    private static final String DONE_MARKER = "1";
    private static final String NOT_DONE_MARKER = "0";
    private static final int MIN_TASK_PARTS = 3;
    private static final int MIN_DEADLINE_PARTS = 4;
    private static final int MIN_EVENT_PARTS = 5;

    // Task type constants
    private static final char TODO_TYPE = 'T';
    private static final char DEADLINE_TYPE = 'D';
    private static final char EVENT_TYPE = 'E';

    // Date format constants
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HHmm";
    private static final String ISO_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm";
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    private static final DateTimeFormatter ISO_FORMAT = DateTimeFormatter.ofPattern(ISO_DATE_TIME_PATTERN);

    private static TasksStorage instance = null;
    private final Path tasksFilePath;

    /**
     * Private constructor for singleton pattern.
     * Initializes data directory and tasks file.
     */
    private TasksStorage() {
        this.tasksFilePath = DATA_DIRECTORY.resolve(TASKS_FILE_NAME);
        initializeStorage();
    }

    /**
     * Initializes the storage directory and file structure.
     *
     * @throws RuntimeException if storage initialization fails
     */
    private void initializeStorage() {
        try {
            createDirectoryIfNotExists();
            createFileIfNotExists();
        } catch (IOException e) {
            System.err.println("Error initializing storage: " + e.getMessage());
            System.err.println("Working directory: " + System.getProperty("user.dir"));
            throw new RuntimeException("Failed to initialize storage", e);
        }
    }

    /**
     * Creates the data directory if it doesn't exist.
     *
     * @throws IOException if directory creation fails
     */
    private void createDirectoryIfNotExists() throws IOException {
        if (Files.notExists(DATA_DIRECTORY)) {
            Files.createDirectories(DATA_DIRECTORY);
            System.out.println("Created data directory: " + DATA_DIRECTORY.toAbsolutePath());
        }
    }

    /**
     * Creates the tasks file if it doesn't exist.
     *
     * @throws IOException if file creation fails
     */
    private void createFileIfNotExists() throws IOException {
        if (Files.notExists(tasksFilePath)) {
            Files.createFile(tasksFilePath);
            System.out.println("Created tasks file: " + tasksFilePath.toAbsolutePath());
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
                if (isValidTaskLine(line)) {
                    try {
                        Task task = parseTaskFromLine(line);
                        if (task != null) {
                            tasks.add(task);
                        }
                    } catch (Exception e) {
                        System.err.println("Skipping corrupted task line: " + line + " (Error: " + e.getMessage() + ")");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Checks if a line is a valid task line.
     *
     * @param line the line to check
     * @return true if the line is valid
     */
    private boolean isValidTaskLine(String line) {
        return line != null && !line.trim().isEmpty();
    }

    /**
     * Parses a task from a line of text.
     *
     * @param line the line to parse
     * @return the parsed task, or null if parsing fails
     * @throws IllegalArgumentException if the line format is invalid
     */
    private Task parseTaskFromLine(String line) {
        String[] parts = line.split(TASK_DELIMITER);
        if (parts.length < MIN_TASK_PARTS) {
            throw new IllegalArgumentException("Insufficient parts in task line");
        }

        char taskType = parts[0].charAt(0);
        boolean isDone = DONE_MARKER.equals(parts[1]);
        String description = parts[2];

        Task task = createTaskByType(taskType, description, parts);

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Creates a task based on its type.
     *
     * @param taskType the type of task
     * @param description the task description
     * @param parts the parsed parts of the task line
     * @return the created task
     * @throws IllegalArgumentException if the task type is unknown or format is invalid
     */
    private Task createTaskByType(char taskType, String description, String[] parts) {
        try {
            switch (taskType) {
            case TODO_TYPE:
                return new TodoTask(description);
            case DEADLINE_TYPE:
                return createDeadlineTask(description, parts);
            case EVENT_TYPE:
                return createEventTask(description, parts);
            default:
                throw new IllegalArgumentException("Unknown task type: " + taskType);
            }
        } catch (FengWeiException e) {
            // Re-throw FengWeiException as IllegalArgumentException to maintain existing error handling
            throw new IllegalArgumentException("Invalid task data: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a deadline task from parsed parts.
     *
     * @param description the task description
     * @param parts the parsed parts
     * @return the deadline task
     * @throws IllegalArgumentException if the format is invalid
     * @throws FengWeiException if task creation fails
     */
    private DeadlineTask createDeadlineTask(String description, String[] parts) throws FengWeiException {
        if (parts.length < MIN_DEADLINE_PARTS) {
            throw new IllegalArgumentException("Deadline task missing 'by' field");
        }
        String by = parts[3];
        return new DeadlineTask(description, by);
    }

    /**
     * Creates an event task from parsed parts.
     *
     * @param description the task description
     * @param parts the parsed parts
     * @return the event task
     * @throws IllegalArgumentException if the format is invalid
     * @throws FengWeiException if task creation fails
     */
    private EventTask createEventTask(String description, String[] parts) throws FengWeiException {
        if (parts.length < MIN_EVENT_PARTS) {
            throw new IllegalArgumentException("Event task missing from/to fields");
        }

        String from = parts[3];
        String to = parts[4];

        LocalDateTime fromDateTime = parseDateTime(from);
        LocalDateTime toDateTime = parseDateTime(to);

        return new EventTask(description, fromDateTime, toDateTime);
    }

    /**
     * Parses a date-time string with fallback formats.
     *
     * @param dateTimeString the date-time string to parse
     * @return the parsed LocalDateTime
     * @throws IllegalArgumentException if parsing fails
     */
    private LocalDateTime parseDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString, INPUT_FORMAT);
        } catch (DateTimeParseException e1) {
            try {
                // Try ISO format as fallback
                return LocalDateTime.parse(dateTimeString, ISO_FORMAT);
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException("Invalid date format: " + dateTimeString);
            }
        }
    }

    /**
     * Saves tasks to the storage file.
     * @param tasks the list of tasks to save
     */
    public void saveTasks(List<Task> tasks) {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(formatTaskForStorage(task));
        }

        try {
            Files.write(tasksFilePath, lines);
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Formats a task for storage.
     *
     * @param task the task to format
     * @return the formatted string
     */
    private String formatTaskForStorage(Task task) {
        StringBuilder line = new StringBuilder();
        line.append(getTaskTypeCharacter(task));
        line.append(" | ");
        line.append(task.isDone() ? DONE_MARKER : NOT_DONE_MARKER);
        line.append(" | ");
        line.append(task.getDescription());

        appendTaskSpecificData(task, line);

        return line.toString();
    }

    /**
     * Gets the character representation of a task type.
     *
     * @param task the task
     * @return the type character
     */
    private char getTaskTypeCharacter(Task task) {
        if (task instanceof TodoTask) {
            return TODO_TYPE;
        } else if (task instanceof DeadlineTask) {
            return DEADLINE_TYPE;
        } else if (task instanceof EventTask) {
            return EVENT_TYPE;
        } else {
            return task.getType();
        }
    }

    /**
     * Appends task-specific data to the storage line.
     *
     * @param task the task
     * @param line the string builder to append to
     */
    private void appendTaskSpecificData(Task task, StringBuilder line) {
        if (task instanceof DeadlineTask) {
            DeadlineTask deadlineTask = (DeadlineTask) task;
            line.append(" | ").append(deadlineTask.formatBy());
        } else if (task instanceof EventTask) {
            EventTask eventTask = (EventTask) task;
            line.append(" | ").append(eventTask.getFrom().format(INPUT_FORMAT));
            line.append(" | ").append(eventTask.getTo().format(INPUT_FORMAT));
        }
    }
}
