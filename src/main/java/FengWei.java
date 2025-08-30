import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FengWei {
    private final Ui ui;

    public FengWei() {
        this.ui = new Ui();
    }

    public static void main(String[] args) {
        new FengWei().run();
    }

    public void run() {
        TasksStorage storage = TasksStorage.getInstance();
        List<Task> taskList = storage.loadTasks();

        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            String command = Parser.getCommand(input);
            String arguments = Parser.getArguments(input);

            if (command.isEmpty()) {
                ui.showLine();
                ui.showError("Invalid command!");
                continue;
            }

            if (command.equals("bye")) {
                break;
            }

            switch (command) {
            case "list":
                ui.showLine();
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskList.size(); i++) {
                    System.out.println((i + 1) + "." + taskList.get(i));
                }
                ui.showLine();
                continue;
            case "todo":
                Task t = new TodoTask(arguments);
                taskList.add(t);
                ui.showLine();
                System.out.println("Got it. I've added this task:");
                System.out.println(" " + t);
                System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                ui.showLine();
                continue;
            case "deadline":
                try {
                    String[] parts = arguments.split(" /by ", 2);
                    if (parts.length < 2) {
                        ui.showLine();
                        ui.showError("The deadline command must be in the format: deadline <description> /by <time>");
                        continue;
                    }
                    String deadlineDesc = parts[0].substring(9).trim();
                    String by = parts[1].trim();
                    Task d = new DeadlineTask(deadlineDesc, by);
                    taskList.add(d);
                    ui.showLine();
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + d);
                    System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                    ui.showLine();
                } catch (DateTimeParseException e) {
                    ui.showLine();
                    ui.showError("The deadline time format is invalid, use YYYY-MM-DD HHMM or a valid date");
                } catch (Exception e) {
                    ui.showLine();
                    ui.showError("Invalid deadline command format!");
                }
                continue;
            case "event":
                try {
                    String[] eventParts = arguments.split(" /from | /to ");
                    if (eventParts.length < 3) {
                        ui.showLine();
                        ui.showError("The event command must be in the format: event <description> /from <start> /to <end>");
                        continue;
                    }
                    String eventDesc = eventParts[0].substring(6).trim();
                    String from = eventParts[1].trim();
                    String to = eventParts[2].trim();
                    DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
                    LocalDateTime fromDateTime = LocalDateTime.parse(from, inputFormat);
                    LocalDateTime toDateTime = LocalDateTime.parse(to, inputFormat);
                    Task e = new EventTask(eventDesc, fromDateTime, toDateTime);
                    taskList.add(e);
                    ui.showLine();
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + e);
                    System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                    ui.showLine();
                } catch (DateTimeParseException e) {
                    ui.showLine();
                    ui.showError("The event time format is invalid, use YYYY-MM-DD HHMM or a valid date");
                } catch (Exception e) {
                    ui.showLine();
                    ui.showError("Invalid event command format!");
                }
                continue;
            case "mark":
                String[] taskMarks = arguments.split(" ");
                try {
                    if (taskMarks.length < 2) {
                        throw new FengWeiException("Please specify the task number to mark.");
                    }
                } catch (FengWeiException e) {
                    ui.showLine();
                    ui.showError(e.getMessage());
                    continue;
                }

                try {
                    if (taskMarks.length > 2) {
                        throw new FengWeiException("Too many arguments");
                    }
                } catch (FengWeiException e) {
                    ui.showLine();
                    ui.showError(e.getMessage());
                    continue;
                }

                int markNumber = Integer.parseInt(taskMarks[1]) - 1;
                taskList.get(markNumber).markAsDone();

                ui.showLine();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("    " + taskList.get(markNumber));
                ui.showLine();
                break;
            case "unmark":
                String[] taskUnmarks = arguments.split(" ");
                int unmarkNumber = Integer.parseInt(taskUnmarks[1]) - 1;
                taskList.get(unmarkNumber).markAsNotDone();
                storage.saveTasks(taskList);
                ui.showLine();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("    " + taskList.get(unmarkNumber));
                ui.showLine();
                storage.saveTasks(taskList);
                continue;
            case "delete":
                String[] parts = arguments.split(" ");
                int deleteNumber = Integer.parseInt(parts[1]) - 1;
                Task removedTask = taskList.remove(deleteNumber);
                ui.showLine();
                System.out.println("Noted. I've removed this task:");
                System.out.println(" " + removedTask);
                System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                ui.showLine();
                storage.saveTasks(taskList);
                continue;
            default:
                ui.showLine();
                ui.showError("Invalid command!");
                Task normal = new Task(input, ' ');
                taskList.add(normal);
                ui.showLine();
                System.out.println("added: " + input);
                ui.showLine();
            }
        }

        ui.showBye();
    }
}
