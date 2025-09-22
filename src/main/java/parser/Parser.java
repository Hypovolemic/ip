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
        assert input != null : "Input should not be null";
        String[] parts = input.trim().split(" ", 2);
        assert parts != null && parts.length > 0 : "Split should always return at least one element";
        return parts[0];
    }

    /**
     * Extracts the arguments from user input.
     * @param input the full user input string
     * @return the arguments part of the input
     */
    public static String getArguments(String input) {
        assert input != null : "Input should not be null";
        String[] parts = input.trim().split(" ", 2);
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
            case "list":
                response = handleListCommandForGui(taskList);
                break;
            case "find":
                response = handleFindCommandForGui(arguments, taskList);
                break;
            case "todo":
                response = handleTodoCommandForGui(arguments, taskList, storage);
                break;
            case "deadline":
                response = handleDeadlineCommandForGui(arguments, taskList, storage);
                break;
            case "event":
                response = handleEventCommandForGui(arguments, taskList, storage);
                break;
            case "mark":
                response = handleMarkCommandForGui(arguments, taskList, storage);
                break;
            case "unmark":
                response = handleUnmarkCommandForGui(arguments, taskList, storage);
                break;
            case "delete":
                response = handleDeleteCommandForGui(arguments, taskList, storage);
                break;
            default:
                response = "OOPS!!! Invalid command!";
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
        assert arguments != null : "Arguments should not be null";
        assert taskList != null : "TaskList should not be null";
        assert storage != null : "Storage should not be null";

        if (arguments.trim().isEmpty()) {
            return "OOPS!!! The description of a todo cannot be empty.";
        }

        int initialSize = taskList.size();
        Task t = new TodoTask(arguments);
        assert t != null : "TodoTask should be successfully created";

        taskList.add(t);
        assert taskList.size() == initialSize + 1 : "TaskList size should increase by 1 after adding task";

        storage.saveTasks(taskList.getAll());
        return "Got it. I've added this task:\n  " + t + "\nNow you have " + taskList.size() + " tasks in the list.";
    }

    private static String handleDeadlineCommandForGui(String arguments, TaskList taskList, TasksStorage storage) {
        try {
            String[] parts = arguments.split(" /by ", 2);
            if (parts.length < 2) {
                return "OOPS!!! The deadline command must be in the format: deadline <description> /by <time>";
            }
            String deadlineDesc = parts[0].trim();
            String by = parts[1].trim();
            if (deadlineDesc.isEmpty()) {
                return "OOPS!!! The description of a deadline cannot be empty.";
            }
            Task d = new DeadlineTask(deadlineDesc, by);
            taskList.add(d);
            storage.saveTasks(taskList.getAll());
            return "Got it. I've added this task:\n  " + d + "\nNow you have " + taskList.size() + " tasks in the list.";
        } catch (DateTimeParseException e) {
            return "OOPS!!! The deadline time format is invalid, use YYYY-MM-DD HHMM or a valid date";
        } catch (Exception e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    private static String handleEventCommandForGui(String arguments, TaskList taskList, TasksStorage storage) {
        try {
            String[] eventParts = arguments.split(" /from | /to ");
            if (eventParts.length < 3) {
                return "OOPS!!! The event command must be in the format: event <description> /from <start> /to <end>";
            }
            String eventDesc = eventParts[0].trim();
            String from = eventParts[1].trim();
            String to = eventParts[2].trim();
            if (eventDesc.isEmpty()) {
                return "OOPS!!! The description of an event cannot be empty.";
            }
            DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            LocalDateTime fromDateTime = LocalDateTime.parse(from, inputFormat);
            LocalDateTime toDateTime = LocalDateTime.parse(to, inputFormat);
            Task e = new EventTask(eventDesc, fromDateTime, toDateTime);
            taskList.add(e);
            storage.saveTasks(taskList.getAll());
            return "Got it. I've added this task:\n  " + e + "\nNow you have " + taskList.size() + " tasks in the list.";
        } catch (DateTimeParseException e) {
            return "OOPS!!! The event time format is invalid, use YYYY-MM-DD HHMM or a valid date";
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
