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
        try {
            this.storage = TasksStorage.getInstance();
            this.taskList = new TaskList(storage.loadTasks());
        } catch (Exception e) {
            System.err.println("Error initializing storage: " + e.getMessage());
            System.err.println("Starting with empty task list...");
            // Still initialize storage even if loading fails
            try {
                this.storage = TasksStorage.getInstance();
            } catch (Exception e2) {
                System.err.println("Critical error: Cannot initialize storage at all!");
                throw new RuntimeException("Failed to initialize storage", e2);
            }
            this.taskList = new TaskList();
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
}
