import parser.Parser;
import storage.TasksStorage;
import tasks.TaskList;
import ui.Ui;

/**
 * Main class for the FengWei task management application.
 * Handles the main application loop and coordinates between UI, Parser, and Storage.
 */
public class FengWei {
    // Application constants
    private static final String COMMAND_BYE = "bye";
    private static final String ERROR_INVALID_COMMAND = "OOPS!!! Invalid command!";
    private static final String ERROR_GENERAL = "OOPS!!! An error occurred: ";
    private static final String MESSAGE_BYE = "Bye. Hope to see you again soon!";

    private final Ui ui;
    private final TasksStorage storage;
    private final TaskList taskList;

    /**
     * Constructs a new FengWei application instance.
     * Initializes UI, storage, and task list components.
     */
    public FengWei() {
        this.ui = new Ui();
        assert ui != null : "UI should be successfully initialized";

        this.storage = initializeStorage();
        this.taskList = initializeTaskList();
    }

    /**
     * Initializes the storage component with error handling.
     *
     * @return TasksStorage instance
     * @throws RuntimeException if storage cannot be initialized
     */
    private TasksStorage initializeStorage() {
        try {
            TasksStorage storage = TasksStorage.getInstance();
            assert storage != null : "Storage should be successfully initialized";
            return storage;
        } catch (Exception e) {
            System.err.println("Error initializing storage: " + e.getMessage());
            throw new RuntimeException("Failed to initialize storage", e);
        }
    }

    /**
     * Initializes the task list with tasks from storage.
     * Falls back to empty list if loading fails.
     *
     * @return TaskList instance
     */
    private TaskList initializeTaskList() {
        try {
            TaskList taskList = new TaskList(storage.loadTasks());
            assert taskList != null : "TaskList should be successfully initialized";
            return taskList;
        } catch (Exception e) {
            System.err.println("Error loading tasks: " + e.getMessage());
            System.err.println("Starting with empty task list...");
            TaskList emptyTaskList = new TaskList();
            assert emptyTaskList != null : "TaskList should be initialized even with empty list";
            return emptyTaskList;
        }
    }

    /**
     * Entry point for the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            new FengWei().run();
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Runs the main application loop.
     * Continuously reads user input and executes commands until the user types "bye".
     */
    public void run() {
        ui.showWelcome();
        processUserCommands();
        ui.showBye();
    }

    /**
     * Processes user commands in a loop until bye command is received.
     */
    private void processUserCommands() {
        while (true) {
            try {
                String input = ui.readCommand();
                String command = Parser.getCommand(input);
                String arguments = Parser.getArguments(input);

                if (isExitCommand(command)) {
                    break;
                }

                if (isValidCommand(command)) {
                    Parser.executeCommand(command, arguments, taskList, storage, ui);
                } else {
                    ui.showError("Invalid command!");
                }
            } catch (Exception e) {
                ui.showError("An error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Checks if the command is an exit command.
     *
     * @param command the command to check
     * @return true if it's an exit command
     */
    private boolean isExitCommand(String command) {
        return COMMAND_BYE.equals(command);
    }

    /**
     * Checks if the command is valid (not empty).
     *
     * @param command the command to check
     * @return true if the command is valid
     */
    private boolean isValidCommand(String command) {
        return !command.isEmpty();
    }

    /**
     * Processes user input and returns a response for the GUI.
     * @param input the user's input command
     * @return the response string to display in the GUI
     */
    public String getResponse(String input) {
        assert input != null : "Input should not be null";
        assert storage != null : "Storage should be initialized before processing commands";
        assert taskList != null : "TaskList should be initialized before processing commands";

        try {
            String command = Parser.getCommand(input);
            String arguments = Parser.getArguments(input);

            assert command != null : "Parser should never return null command";
            assert arguments != null : "Parser should never return null arguments";

            if (command.isEmpty()) {
                return "OOPS!!! Invalid command!";
            }

            if (isExitCommand(command)) {
                return MESSAGE_BYE;
            }

            String response = Parser.executeCommandForGui(command, arguments, taskList, storage);
            assert response != null : "Parser should never return null response";
            return response;
        } catch (Exception e) {
            return ERROR_GENERAL + e.getMessage();
        }
    }

    /**
     * Gets the welcome message for GUI display.
     * @return the welcome message string
     */
    public String getWelcomeMessage() {
        assert ui != null : "UI should be initialized before getting welcome message";
        String message = ui.getWelcomeMessage();
        assert message != null && !message.isEmpty() : "Welcome message should not be null or empty";
        return message;
    }
}
