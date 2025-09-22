import parser.Parser;
import storage.TasksStorage;
import tasks.TaskList;
import ui.Ui;

/**
 * Main class for the FengWei task management application.
 * Handles the main application loop and coordinates between UI, Parser, and Storage.
 */
public class FengWei {
    private final Ui ui;
    private TasksStorage storage;
    private TaskList taskList;

    public FengWei() {
        this.ui = new Ui();
        assert ui != null : "UI should be successfully initialized";

        try {
            this.storage = TasksStorage.getInstance();
            this.taskList = new TaskList(storage.loadTasks());
            assert storage != null : "Storage should be successfully initialized";
            assert taskList != null : "TaskList should be successfully initialized";
        } catch (Exception e) {
            System.err.println("Error initializing storage: " + e.getMessage());
            System.err.println("Starting with empty task list...");
            // Still initialize storage even if loading fails
            try {
                this.storage = TasksStorage.getInstance();
                assert storage != null : "Storage should be initialized even after loading failure";
            } catch (Exception e2) {
                System.err.println("Critical error: Cannot initialize storage at all!");
                throw new RuntimeException("Failed to initialize storage", e2);
            }
            this.taskList = new TaskList();
            assert taskList != null : "TaskList should be initialized even with empty list";
        }
    }

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

        while (true) {
            try {
                String input = ui.readCommand();
                String command = Parser.getCommand(input);
                String arguments = Parser.getArguments(input);

                if (command.isEmpty()) {
                    ui.showError("Invalid command!");
                    continue;
                }

                if (command.equals("bye")) {
                    break;
                }

                Parser.executeCommand(command, arguments, taskList, storage, ui);
            } catch (Exception e) {
                ui.showError("An error occurred: " + e.getMessage());
            }
        }

        ui.showBye();
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

            if (command.equals("bye")) {
                return "Bye. Hope to see you again soon!";
            }

            String response = Parser.executeCommandForGui(command, arguments, taskList, storage);
            assert response != null : "Parser should never return null response";
            return response;
        } catch (Exception e) {
            return "OOPS!!! An error occurred: " + e.getMessage();
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
