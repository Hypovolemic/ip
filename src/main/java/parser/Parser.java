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
    private static final String COMMAND_HELP = "help";
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
        assert input != null : "Input should not be null";
        String[] parts = input.trim().split(" ", COMMAND_SPLIT_LIMIT);
        assert parts != null && parts.length > 0 : "Split should always return at least one element";
        return parts[0].toLowerCase();
    }

    /**
     * Extracts the arguments from user input.
     * @param input the full user input string
     * @return the arguments part of the input
     */
    public static String getArguments(String input) {
        // Combine both approaches: null checking from CodeQuality + assertions from Assertions
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        assert input != null : "Input should not be null";
        String[] parts = input.trim().split(" ", COMMAND_SPLIT_LIMIT);
        assert parts != null && parts.length > 0 : "Split should always return at least one element";
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
        assert command != null : "Command should not be null";
        assert arguments != null : "Arguments should not be null";
        assert taskList != null : "TaskList should not be null";
        assert storage != null : "Storage should not be null";
        assert ui != null : "UI should not be null";

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
        case COMMAND_HELP:
            handleHelpCommand(ui);
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
        try {
            Task t = new TodoTask(arguments);
            taskList.add(t);
            storage.saveTasks(taskList.getAll());
            ui.showTaskAdded(t, taskList.size());
        } catch (FengWeiException e) {
            ui.showError(e.getMessage());
        }
    }

    private static void handleDeadlineCommand(String arguments, TaskList taskList, Ui ui, TasksStorage storage) {
        try {
            String[] parts = arguments.split(DEADLINE_DELIMITER, 2);
            if (parts.length < 2) {
                throw new FengWeiException(ERROR_DEADLINE_FORMAT);
            }
            String deadlineDesc = parts[0].trim();
            String by = parts[1].trim();

            if (deadlineDesc.isEmpty()) {
                throw new FengWeiException(ERROR_EMPTY_DEADLINE);
            }

            Task d = new DeadlineTask(deadlineDesc, by);
            taskList.add(d);
            storage.saveTasks(taskList.getAll());
            ui.showTaskAdded(d, taskList.size());
        } catch (FengWeiException e) {
            ui.showError(e.getMessage());
        } catch (DateTimeParseException e) {
            ui.showError(ERROR_INVALID_DATE);
        }
    }

    private static void handleEventCommand(String arguments, TaskList taskList, Ui ui, TasksStorage storage) {
        try {
            String[] eventParts = arguments.split(EVENT_DELIMITER_PATTERN);
            if (eventParts.length < 3) {
                throw new FengWeiException(ERROR_EVENT_FORMAT);
            }
            String eventDesc = eventParts[0].trim();
            String from = eventParts[1].trim();
            String to = eventParts[2].trim();

            if (eventDesc.isEmpty()) {
                throw new FengWeiException(ERROR_EMPTY_EVENT);
            }

            LocalDateTime fromDateTime = LocalDateTime.parse(from, DATE_TIME_FORMATTER);
            LocalDateTime toDateTime = LocalDateTime.parse(to, DATE_TIME_FORMATTER);
            Task e = new EventTask(eventDesc, fromDateTime, toDateTime);
            taskList.add(e);
            storage.saveTasks(taskList.getAll());
            ui.showTaskAdded(e, taskList.size());
        } catch (FengWeiException e) {
            ui.showError(e.getMessage());
        } catch (DateTimeParseException e) {
            ui.showError(ERROR_INVALID_DATE);
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
     * Handles the help command by displaying usage information.
     * @param ui the user interface for output
     */
    private static void handleHelpCommand(Ui ui) {
        ui.showHelp();
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
        assert command != null : "Command should not be null";
        assert arguments != null : "Arguments should not be null";
        assert taskList != null : "TaskList should not be null";
        assert storage != null : "Storage should not be null";

        try {
            String response;
            switch (command) {
            case COMMAND_LIST:
                response = handleListCommandForGui(taskList);
                break;
            case COMMAND_FIND:
                response = handleFindCommandForGui(arguments, taskList);
                break;
            case COMMAND_TODO:
                response = handleTodoCommandForGui(arguments, taskList, storage);
                break;
            case COMMAND_DEADLINE:
                response = handleDeadlineCommandForGui(arguments, taskList, storage);
                break;
            case COMMAND_EVENT:
                response = handleEventCommandForGui(arguments, taskList, storage);
                break;
            case COMMAND_MARK:
                response = handleMarkCommandForGui(arguments, taskList, storage);
                break;
            case COMMAND_UNMARK:
                response = handleUnmarkCommandForGui(arguments, taskList, storage);
                break;
            case COMMAND_DELETE:
                response = handleDeleteCommandForGui(arguments, taskList, storage);
                break;
            case COMMAND_HELP:
                response = handleHelpCommandForGui();
                break;
            default:
                response = ERROR_INVALID_COMMAND;
            }
            assert response != null : "Response should never be null";
            return response;
        } catch (Exception e) {
            return "OOPS!!! An error occurred: " + e.getMessage();
        }
    }

    private static String handleListCommandForGui(TaskList taskList) {
        assert taskList != null : "TaskList should not be null";
        if (taskList.size() == 0) {
            return "Your task list is empty!";
        }
        StringBuilder response = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < taskList.size(); i++) {
            assert taskList.get(i) != null : "Task at index " + i + " should not be null";
            response.append((i + 1)).append(".").append(taskList.get(i)).append("\n");
        }
        assert response.length() > 0 : "Response should not be empty for non-empty task list";
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
        try {
            Task t = new TodoTask(arguments);
            taskList.add(t);
            storage.saveTasks(taskList.getAll());
            return "Got it. I've added this task:\n  " + t + "\nNow you have " + taskList.size() + " tasks in the list.";
        } catch (FengWeiException e) {
            return e.getMessage();
        }
    }

    private static String handleDeadlineCommandForGui(String arguments, TaskList taskList, TasksStorage storage) {
        try {
            String[] parts = arguments.split(DEADLINE_DELIMITER, 2);
            if (parts.length < 2) {
                throw new FengWeiException(ERROR_DEADLINE_FORMAT);
            }
            String deadlineDesc = parts[0].trim();
            String by = parts[1].trim();

            if (deadlineDesc.isEmpty()) {
                throw new FengWeiException(ERROR_EMPTY_DEADLINE);
            }

            Task d = new DeadlineTask(deadlineDesc, by);
            taskList.add(d);
            storage.saveTasks(taskList.getAll());
            return "Got it. I've added this task:\n  " + d + "\nNow you have " + taskList.size() + " tasks in the list.";
        } catch (FengWeiException e) {
            return e.getMessage();
        } catch (DateTimeParseException e) {
            return ERROR_INVALID_DATE;
        }
    }

    private static String handleEventCommandForGui(String arguments, TaskList taskList, TasksStorage storage) {
        try {
            String[] eventParts = arguments.split(EVENT_DELIMITER_PATTERN);
            if (eventParts.length < 3) {
                throw new FengWeiException(ERROR_EVENT_FORMAT);
            }
            String eventDesc = eventParts[0].trim();
            String from = eventParts[1].trim();
            String to = eventParts[2].trim();

            if (eventDesc.isEmpty()) {
                throw new FengWeiException(ERROR_EMPTY_EVENT);
            }

            LocalDateTime fromDateTime = LocalDateTime.parse(from, DATE_TIME_FORMATTER);
            LocalDateTime toDateTime = LocalDateTime.parse(to, DATE_TIME_FORMATTER);
            Task e = new EventTask(eventDesc, fromDateTime, toDateTime);
            taskList.add(e);
            storage.saveTasks(taskList.getAll());
            return "Got it. I've added this task:\n  " + e + "\nNow you have " + taskList.size() + " tasks in the list.";
        } catch (FengWeiException e) {
            return e.getMessage();
        } catch (DateTimeParseException e) {
            return ERROR_INVALID_DATE;
        }
    }

    private static String handleMarkCommandForGui(String arguments, TaskList taskList, TasksStorage storage) {
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
            return "Nice! I've marked this task as done:\n    " + taskList.get(markNumber);
        } catch (FengWeiException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return ERROR_INVALID_TASK_NUMBER;
        } catch (IndexOutOfBoundsException e) {
            return "Invalid task number! Please enter a number between 1 and " + taskList.size();
        }
    }

    private static String handleUnmarkCommandForGui(String arguments, TaskList taskList, TasksStorage storage) {
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
            return "OK, I've marked this task as not done yet:\n    " + taskList.get(unmarkNumber);
        } catch (FengWeiException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return ERROR_INVALID_TASK_NUMBER;
        } catch (IndexOutOfBoundsException e) {
            return "Invalid task number! Please enter a number between 1 and " + taskList.size();
        }
    }

    private static String handleDeleteCommandForGui(String arguments, TaskList taskList, TasksStorage storage) {
        try {
            if (arguments.trim().isEmpty()) {
                throw new FengWeiException("OOPS!!! Please specify the task number to delete.");
            }

            String[] parts = arguments.trim().split("\\s+");
            if (parts.length != 1) {
                throw new FengWeiException("OOPS!!! Please specify only one task number to delete.");
            }

            int deleteNumber = Integer.parseInt(parts[0]) - 1;
            if (deleteNumber < 0 || deleteNumber >= taskList.size()) {
                throw new IndexOutOfBoundsException("Task number out of range.");
            }

            Task removedTask = taskList.remove(deleteNumber);
            storage.saveTasks(taskList.getAll());
            return "Noted. I've removed this task:\n " + removedTask + "\nNow you have " + taskList.size() + " tasks in the list.";
        } catch (FengWeiException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return "OOPS!!! Please enter a valid task number!";
        } catch (IndexOutOfBoundsException e) {
            return "Invalid task number! Please enter a number between 1 and " + taskList.size();
        }
    }

    private static String handleHelpCommandForGui() {
        return "Here are the available commands:\n"
                + "1. list - Shows the list of tasks.\n"
                + "2. find <keyword> - Finds tasks matching the keyword.\n"
                + "3. todo <description> - Adds a todo task.\n"
                + "4. deadline <description> /by <time> - Adds a deadline task.\n"
                + "5. event <description> /from <start> /to <end> - Adds an event task.\n"
                + "6. mark <task number> - Marks a task as done.\n"
                + "7. unmark <task number> - Marks a task as not done.\n"
                + "8. delete <task number> - Deletes a task from the list.\n"
                + "9. help - Shows this help message.\n"
                + "10. bye - Exits the program.";
    }
}
