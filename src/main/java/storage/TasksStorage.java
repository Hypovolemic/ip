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
    private final Path TasksFilePath = Paths.get(DATA_DIRECTORY.toString(), "Tasks.txt");

    private TasksStorage() {
        try {
            if (Files.notExists(DATA_DIRECTORY)) {
                Files.createDirectories(DATA_DIRECTORY);
            }
            if (Files.notExists(TasksFilePath)) {
                Files.createFile(TasksFilePath);
            }
        } catch (IOException e) {
            System.out.println("Error initializing storage: " + e.getMessage());
        }
    }

    public static TasksStorage getInstance() {
        if (instance == null) {
            instance = new TasksStorage();
        }
        return instance;
    }

    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(TasksFilePath);
            for (String line : lines) {
                String[] parts = line.split(" \\| ");
                char taskType = parts[0].charAt(0);
                boolean isDone = parts[1].equals("1");
                String description = parts[2];

                Task task;
                switch (taskType) {
                case 'T':
                    task = new Task(description, 'T');
                    break;
                case 'D':
                    String by = parts[3];
                    task = new DeadlineTask(description, by);
                    break;
                case 'E':
                    if (parts.length < 5) {
                        System.out.println("Invalid event task format: " + line);
                        continue; // or handle as needed
                    }
                    String from = parts[3];
                    String to = parts[4];
                    try {
                        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
                        LocalDateTime fromDateTime = LocalDateTime.parse(from, inputFormat);
                        LocalDateTime toDateTime = LocalDateTime.parse(to, inputFormat);
                        task = new EventTask(description, fromDateTime, toDateTime);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format in event task: " + line);
                        continue;
                    }
                    break;
                default:
                    continue; // Skip unknown task types
                }

                if (isDone) {
                    task.markAsDone();
                }
                tasks.add(task);
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
        return tasks;
    }

    public void saveTasks(List<Task> tasks) {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            StringBuilder line = new StringBuilder();
            line.append(task instanceof TodoTask ? 'T' : task instanceof DeadlineTask ? 'D' : 'E');
            line.append(" | ");
            line.append(task.isDone() ? "1" : "0");
            line.append(" | ");
            line.append(task.toString().substring(7)); // Skip the "[T][ ] " part

            if (task instanceof DeadlineTask) {
                line.append(" | ").append(((DeadlineTask) task).formatBy());
            } else if (task instanceof EventTask) {
                line.append(" | ").append(((EventTask) task).getFrom());
                line.append(" | ").append(((EventTask) task).getTo());
            }

            lines.add(line.toString());
        }

        try {
            Files.write(TasksFilePath, lines);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}
