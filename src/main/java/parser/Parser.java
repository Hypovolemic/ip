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
    // Command constants
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_FIND = "find";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_BYE = "bye";

    // Split limits
    private static final int COMMAND_SPLIT_LIMIT = 2;

    // Date format constants
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HHmm";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    // Error messages
    private static final String ERROR_INVALID_COMMAND = "OOPS!!! Invalid command!";
    private static final String ERROR_EMPTY_TODO = "OOPS!!! The description of a todo cannot be empty.";
    private static final String ERROR_DEADLINE_FORMAT = "OOPS!!! The deadline command must be in the format: deadline <description> /by <time>";
    private static final String ERROR_EVENT_FORMAT = "OOPS!!! The event command must be in the format: event <description> /from <start> /to <end>";
    private static final String ERROR_EMPTY_DEADLINE = "OOPS!!! The description of a deadline cannot be empty.";
    private static final String ERROR_EMPTY_EVENT = "OOPS!!! The description of an event cannot be empty.";
    private static final String ERROR_INVALID_DATE = "OOPS!!! The date format is invalid, use YYYY-MM-DD HHMM";
    private static final String ERROR_SPECIFY_TASK_NUMBER = "OOPS!!! Please specify the task number";
    private static final String ERROR_SINGLE_TASK_NUMBER = "OOPS!!! Please specify only one task number";
    private static final String ERROR_INVALID_TASK_NUMBER = "OOPS!!! Please enter a valid task number!";

    // Delimiter constants
    private static final String DEADLINE_DELIMITER = " /by ";
    private static final String EVENT_DELIMITER_PATTERN = " /from | /to ";

    /**
     * Extracts the command word from user input.
     * @param input the full user input string
     * @return the command word
     */
    public static String getCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        String[] parts = input.trim().split(" ", COMMAND_SPLIT_LIMIT);
        return parts[0].toLowerCase();
    }

    /**
     * Extracts the arguments from user input.
     * @param input the full user input string
     * @return the arguments part of the input
     */
    public static String getArguments(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        String[] parts = input.trim().split(" ", COMMAND_SPLIT_LIMIT);
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
        case COMMAND_LIST:
            handleListCommand(taskList, ui);
            break;
        case COMMAND_FIND:
            handleFindCommand(arguments, taskList, ui);
            break;
        case COMMAND_TODO:
            handleTodoCommand(arguments, taskList, ui, storage);
            break;
        case COMMAND_DEADLINE:
            handleDeadlineCommand(arguments, taskList, ui, storage);
            break;
        case COMMAND_EVENT:
            handleEventCommand(arguments, taskList, ui, storage);
            break;
        case COMMAND_MARK:
            handleMarkCommand(arguments, taskList, storage, ui);
            break;
        case COMMAND_UNMARK:
            handleUnmarkCommand(arguments, taskList, storage, ui);
            break;
        case COMMAND_DELETE:
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
            String[] parts = arguments.split(DEADLINE_DELIMITER, 2);
            if (parts.length < 2) {
                ui.showError(ERROR_DEADLINE_FORMAT);
                return;
            }
            String deadlineDesc = parts[0].trim();
            String by = parts[1].trim();
            Task d = new DeadlineTask(deadlineDesc, by);
            taskList.add(d);
            storage.saveTasks(taskList.getAll());
            ui.showTaskAdded(d, taskList.size());
        } catch (DateTimeParseException e) {
            ui.showError(ERROR_INVALID_DATE);
        } catch (Exception e) {
            ui.showError("Invalid deadline command format!");
        }
    }

    private static void handleEventCommand(String arguments, TaskList taskList, Ui ui, TasksStorage storage) {
        try {
            String[] eventParts = arguments.split(EVENT_DELIMITER_PATTERN);
            if (eventParts.length < 3) {
                ui.showError(ERROR_EVENT_FORMAT);
                return;
            }
            String eventDesc = eventParts[0].trim();
            String from = eventParts[1].trim();
            String to = eventParts[2].trim();
            LocalDateTime fromDateTime = LocalDateTime.parse(from, DATE_TIME_FORMATTER);
            LocalDateTime toDateTime = LocalDateTime.parse(to, DATE_TIME_FORMATTER);
            Task e = new EventTask(eventDesc, fromDateTime, toDateTime);
            taskList.add(e);
            storage.saveTasks(taskList.getAll());
            ui.showTaskAdded(e, taskList.size());
        } catch (DateTimeParseException e) {
            ui.showError(ERROR_INVALID_DATE);
        } catch (Exception e) {
            ui.showError("Invalid event command format!");
        }
    }

    private static void handleMarkCommand(String arguments, TaskList taskList,
                                        TasksStorage storage, Ui ui) {
        try {
            if (arguments.trim().isEmpty()) {
                throw new FengWeiException(ERROR_SPECIFY_TASK_NUMBER);
            }

            String[] parts = arguments.trim().split("\\s+");
            if (parts.length != 1) {
                throw new FengWeiException(ERROR_SINGLE_TASK_NUMBER);
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
            ui.showError(ERROR_INVALID_TASK_NUMBER);
        } catch (IndexOutOfBoundsException e) {
            ui.showError("Invalid task number! Please enter a number between 1 and " + taskList.size());
        }
    }

    private static void handleUnmarkCommand(String arguments, TaskList taskList,
                                          TasksStorage storage, Ui ui) {
        try {
            if (arguments.trim().isEmpty()) {
                throw new FengWeiException(ERROR_SPECIFY_TASK_NUMBER);
            }

            String[] parts = arguments.trim().split("\\s+");
            if (parts.length != 1) {
                throw new FengWeiException(ERROR_SINGLE_TASK_NUMBER);
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
            ui.showError(ERROR_INVALID_TASK_NUMBER);
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
        ui.showError(ERROR_INVALID_COMMAND);
        Task normal = new Task(input, ' ');
        taskList.add(normal);
        ui.showLine();
        System.out.println("added: " + input);
        ui.showLine();
    }

    /**
     * Executes a command and returns a string response for GUI display.
     * @param command the command to execute
     * @param arguments the arguments for the command
     * @param taskList the task list to operate on
     * @param storage the storage to save tasks
     * @return the response string for GUI display
     */
    public static String executeCommandForGui(String command, String arguments, TaskList taskList,
                                            TasksStorage storage) {
        try {
            switch (command) {
            case COMMAND_LIST:
                return handleListCommandForGui(taskList);
            case COMMAND_FIND:
                return handleFindCommandForGui(arguments, taskList);
            case COMMAND_TODO:
                return handleTodoCommandForGui(arguments, taskList, storage);
            case COMMAND_DEADLINE:
                return handleDeadlineCommandForGui(arguments, taskList, storage);
            case COMMAND_EVENT:
                return handleEventCommandForGui(arguments, taskList, storage);
            case COMMAND_MARK:
                return handleMarkCommandForGui(arguments, taskList, storage);
            case COMMAND_UNMARK:
                return handleUnmarkCommandForGui(arguments, taskList, storage);
            case COMMAND_DELETE:
                return handleDeleteCommandForGui(arguments, taskList, storage);
            default:
                return ERROR_INVALID_COMMAND;
            }
        } catch (Exception e) {
            return "OOPS!!! An error occurred: " + e.getMessage();
        }
    }

    private static String handleListCommandForGui(TaskList taskList) {
        if (taskList.size() == 0) {
            return "Your task list is empty!";
        }
        StringBuilder response = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < taskList.size(); i++) {
            response.append((i + 1)).append(".").append(taskList.get(i)).append("\n");
        }
        return response.toString();
    }

    private static String handleFindCommandForGui(String arguments, TaskList taskList) {
        List<Task> found = taskList.findTasks(arguments);
        if (found.isEmpty()) {
            return "No matching tasks found!";
        }
        StringBuilder response = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < found.size(); i++) {
            response.append((i + 1)).append(".").append(found.get(i)).append("\n");
        }
        return response.toString();
    }

    private static String handleTodoCommandForGui(String arguments, TaskList taskList, TasksStorage storage) {
        if (arguments.trim().isEmpty()) {
            return ERROR_EMPTY_TODO;
        }
        Task t = new TodoTask(arguments);
        taskList.add(t);
        storage.saveTasks(taskList.getAll());
        return "Got it. I've added this task:\n  " + t + "\nNow you have " + taskList.size() + " tasks in the list.";
    }

    private static String handleDeadlineCommandForGui(String arguments, TaskList taskList, TasksStorage storage) {
        try {
            String[] parts = arguments.split(" /by ", 2);
            if (parts.length < 2) {
                return ERROR_DEADLINE_FORMAT;
            }
            String deadlineDesc = parts[0].trim();
            String by = parts[1].trim();
            if (deadlineDesc.isEmpty()) {
                return ERROR_EMPTY_DEADLINE;
            }
            Task d = new DeadlineTask(deadlineDesc, by);
            taskList.add(d);
            storage.saveTasks(taskList.getAll());
            return "Got it. I've added this task:\n  " + d + "\nNow you have " + taskList.size() + " tasks in the list.";
        } catch (DateTimeParseException e) {
            return ERROR_INVALID_DATE;
        } catch (Exception e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    private static String handleEventCommandForGui(String arguments, TaskList taskList, TasksStorage storage) {
        try {
            String[] eventParts = arguments.split(" /from | /to ");
            if (eventParts.length < 3) {
                return ERROR_EVENT_FORMAT;
            }
            String eventDesc = eventParts[0].trim();
            String from = eventParts[1].trim();
            String to = eventParts[2].trim();
            if (eventDesc.isEmpty()) {
                return ERROR_EMPTY_EVENT;
            }
            LocalDateTime fromDateTime = LocalDateTime.parse(from, DATE_TIME_FORMATTER);
            LocalDateTime toDateTime = LocalDateTime.parse(to, DATE_TIME_FORMATTER);
            Task e = new EventTask(eventDesc, fromDateTime, toDateTime);
            taskList.add(e);
            storage.saveTasks(taskList.getAll());
            return "Got it. I've added this task:\n  " + e + "\nNow you have " + taskList.size() + " tasks in the list.";
        } catch (DateTimeParseException e) {
            return ERROR_INVALID_DATE;
        } catch (Exception e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    private static String handleMarkCommandForGui(String arguments, TaskList taskList, TasksStorage storage) {
        try {
            if (arguments.trim().isEmpty()) {
                return "OOPS!!! Please specify the task number to mark.";
            }
            String[] parts = arguments.trim().split("\\s+");
            if (parts.length != 1) {
                return "OOPS!!! Please specify only one task number to mark.";
            }
            int markNumber = Integer.parseInt(parts[0]) - 1;
            if (markNumber < 0 || markNumber >= taskList.size()) {
                return "OOPS!!! Invalid task number! Please enter a number between 1 and " + taskList.size();
            }
            taskList.markAsDone(markNumber);
            storage.saveTasks(taskList.getAll());
            return "Nice! I've marked this task as done:\n    " + taskList.get(markNumber);
        } catch (NumberFormatException e) {
            return "OOPS!!! Please enter a valid task number!";
        } catch (Exception e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    private static String handleUnmarkCommandForGui(String arguments, TaskList taskList, TasksStorage storage) {
        try {
            if (arguments.trim().isEmpty()) {
                return "OOPS!!! Please specify the task number to unmark.";
            }
            String[] parts = arguments.trim().split("\\s+");
            if (parts.length != 1) {
                return "OOPS!!! Please specify only one task number to unmark.";
            }
            int unmarkNumber = Integer.parseInt(parts[0]) - 1;
            if (unmarkNumber < 0 || unmarkNumber >= taskList.size()) {
                return "OOPS!!! Invalid task number! Please enter a number between 1 and " + taskList.size();
            }
            taskList.markAsNotDone(unmarkNumber);
            storage.saveTasks(taskList.getAll());
            return "OK, I've marked this task as not done yet:\n    " + taskList.get(unmarkNumber);
        } catch (NumberFormatException e) {
            return "OOPS!!! Please enter a valid task number!";
        } catch (Exception e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    private static String handleDeleteCommandForGui(String arguments, TaskList taskList, TasksStorage storage) {
        try {
            if (arguments.trim().isEmpty()) {
                return "OOPS!!! Please specify the task number to delete.";
            }
            String[] parts = arguments.trim().split("\\s+");
            if (parts.length != 1) {
                return "OOPS!!! Please specify only one task number to delete.";
            }
            int deleteNumber = Integer.parseInt(parts[0]) - 1;
            if (deleteNumber < 0 || deleteNumber >= taskList.size()) {
                return "OOPS!!! Invalid task number! Please enter a number between 1 and " + taskList.size();
            }
            Task removedTask = taskList.remove(deleteNumber);
            storage.saveTasks(taskList.getAll());
            return "Noted. I've removed this task:\n " + removedTask + "\nNow you have " + taskList.size() + " tasks in the list.";
        } catch (NumberFormatException e) {
            return "OOPS!!! Please enter a valid task number!";
        } catch (Exception e) {
            return "OOPS!!! " + e.getMessage();
        }
    }
}
