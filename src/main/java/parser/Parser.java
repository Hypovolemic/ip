package parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import exceptions.FengWeiException;
import storage.TasksStorage;
import tasks.DeadlineTask;
import tasks.EventTask;
import tasks.Task;
import tasks.TaskList;
import tasks.TodoTask;
import ui.Ui;

/**
 * Utility class for parsing user input commands and their arguments.
 */
public class Parser {
    /**
     * Extracts the command word from user input.
     * @param input the full user input string
     * @return the command word
     */
    public static String getCommand(String input) {
        String[] parts = input.trim().split(" ", 2);
        return parts[0];
    }

    /**
     * Extracts the arguments from user input.
     * @param input the full user input string
     * @return the arguments part of the input
     */
    public static String getArguments(String input) {
        String[] parts = input.trim().split(" ", 2);
        return parts.length > 1 ? parts[1] : "";
    }

    /**
     * Executes the appropriate command based on user input.
     * @param command the command to execute
     * @param arguments the arguments for the command
     * @param taskList the task list to operate on
     * @param storage the storage to save tasks
     * @param ui the user interface for output
     */
    public static void executeCommand(String command, String arguments, TaskList taskList,
                                    TasksStorage storage, Ui ui) {
        switch (command) {
        case "list":
            handleListCommand(taskList, ui);
            break;
        case "find":
            handleFindCommand(arguments, taskList, ui);
            break;
        case "todo":
            handleTodoCommand(arguments, taskList, ui, storage);
            break;
        case "deadline":
            handleDeadlineCommand(arguments, taskList, ui, storage);
            break;
        case "event":
            handleEventCommand(arguments, taskList, ui, storage);
            break;
        case "mark":
            handleMarkCommand(arguments, taskList, storage, ui);
            break;
        case "unmark":
            handleUnmarkCommand(arguments, taskList, storage, ui);
            break;
        case "delete":
            handleDeleteCommand(arguments, taskList, storage, ui);
            break;
        default:
            handleInvalidCommand(command, taskList, ui);
        }
    }

    private static void handleListCommand(TaskList taskList, Ui ui) {
        ui.showTaskList(taskList.getAll());
    }

    private static void handleFindCommand(String arguments, TaskList taskList, Ui ui) {
        List<Task> found = taskList.findTasks(arguments);
        ui.showFoundTasks(found);
    }

    private static void handleTodoCommand(String arguments, TaskList taskList, Ui ui, TasksStorage storage) {
        Task t = new TodoTask(arguments);
        taskList.add(t);
        storage.saveTasks(taskList.getAll());
        ui.showTaskAdded(t, taskList.size());
    }

    private static void handleDeadlineCommand(String arguments, TaskList taskList, Ui ui, TasksStorage storage) {
        try {
            String[] parts = arguments.split(" /by ", 2);
            if (parts.length < 2) {
                ui.showError("The deadline command must be in the format: deadline <description> /by <time>");
                return;
            }
            String deadlineDesc = parts[0].trim();
            String by = parts[1].trim();
            Task d = new DeadlineTask(deadlineDesc, by);
            taskList.add(d);
            storage.saveTasks(taskList.getAll());
            ui.showTaskAdded(d, taskList.size());
        } catch (DateTimeParseException e) {
            ui.showError("The deadline time format is invalid, use YYYY-MM-DD HHMM or a valid date");
        } catch (Exception e) {
            ui.showError("Invalid deadline command format!");
        }
    }

    private static void handleEventCommand(String arguments, TaskList taskList, Ui ui, TasksStorage storage) {
        try {
            String[] eventParts = arguments.split(" /from | /to ");
            if (eventParts.length < 3) {
                ui.showError("The event command must be in the format: event <description> /from <start> /to <end>");
                return;
            }
            String eventDesc = eventParts[0].trim();
            String from = eventParts[1].trim();
            String to = eventParts[2].trim();
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            LocalDateTime fromDateTime = LocalDateTime.parse(from, inputFormat);
            LocalDateTime toDateTime = LocalDateTime.parse(to, inputFormat);
            Task e = new EventTask(eventDesc, fromDateTime, toDateTime);
            taskList.add(e);
            storage.saveTasks(taskList.getAll());
            ui.showTaskAdded(e, taskList.size());
        } catch (DateTimeParseException e) {
            ui.showError("The event time format is invalid, use YYYY-MM-DD HHMM or a valid date");
        } catch (Exception e) {
            ui.showError("Invalid event command format!");
        }
    }

    private static void handleMarkCommand(String arguments, TaskList taskList,
                                        TasksStorage storage, Ui ui) {
        try {
            if (arguments.trim().isEmpty()) {
                throw new FengWeiException("Please specify the task number to mark.");
            }

            String[] parts = arguments.trim().split("\\s+");
            if (parts.length != 1) {
                throw new FengWeiException("Please specify only one task number to mark.");
            }

            int markNumber = Integer.parseInt(parts[0]) - 1;
            if (markNumber < 0 || markNumber >= taskList.size()) {
                throw new IndexOutOfBoundsException("Task number out of range.");
            }

            taskList.markAsDone(markNumber);
            storage.saveTasks(taskList.getAll());
            ui.showTaskMarked(taskList.get(markNumber));
        } catch (FengWeiException e) {
            ui.showError(e.getMessage());
        } catch (NumberFormatException e) {
            ui.showError("Please enter a valid task number!");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("Invalid task number! Please enter a number between 1 and " + taskList.size());
        }
    }

    private static void handleUnmarkCommand(String arguments, TaskList taskList,
                                          TasksStorage storage, Ui ui) {
        try {
            if (arguments.trim().isEmpty()) {
                throw new FengWeiException("Please specify the task number to unmark.");
            }

            String[] parts = arguments.trim().split("\\s+");
            if (parts.length != 1) {
                throw new FengWeiException("Please specify only one task number to unmark.");
            }

            int unmarkNumber = Integer.parseInt(parts[0]) - 1;
            if (unmarkNumber < 0 || unmarkNumber >= taskList.size()) {
                throw new IndexOutOfBoundsException("Task number out of range.");
            }

            taskList.markAsNotDone(unmarkNumber);
            storage.saveTasks(taskList.getAll());
            ui.showTaskUnmarked(taskList.get(unmarkNumber));
        } catch (FengWeiException e) {
            ui.showError(e.getMessage());
        } catch (NumberFormatException e) {
            ui.showError("Please enter a valid task number!");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("Invalid task number! Please enter a number between 1 and " + taskList.size());
        }
    }

    private static void handleDeleteCommand(String arguments, TaskList taskList,
                                          TasksStorage storage, Ui ui) {
        try {
            if (arguments.trim().isEmpty()) {
                throw new FengWeiException("Please specify the task number to delete.");
            }

            String[] parts = arguments.trim().split("\\s+");
            if (parts.length != 1) {
                throw new FengWeiException("Please specify only one task number to delete.");
            }

            int deleteNumber = Integer.parseInt(parts[0]) - 1;
            if (deleteNumber < 0 || deleteNumber >= taskList.size()) {
                throw new IndexOutOfBoundsException("Task number out of range.");
            }

            Task removedTask = taskList.remove(deleteNumber);
            storage.saveTasks(taskList.getAll());
            ui.showTaskDeleted(removedTask, taskList.size());
        } catch (FengWeiException e) {
            ui.showError(e.getMessage());
        } catch (NumberFormatException e) {
            ui.showError("Please enter a valid task number!");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("Invalid task number! Please enter a number between 1 and " + taskList.size());
        }
    }

    private static void handleInvalidCommand(String input, TaskList taskList, Ui ui) {
        ui.showError("Invalid command!");
        Task normal = new Task(input, ' ');
        taskList.add(normal);
        ui.showLine();
        System.out.println("added: " + input);
        ui.showLine();
    }
}
