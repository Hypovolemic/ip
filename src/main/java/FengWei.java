import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class FengWei {
    public static void main(String[] args) throws FengWeiException {
        Scanner scanner = new Scanner(System.in);
        TasksStorage storage = TasksStorage.getInstance();
        List<Task> taskList = storage.loadTasks(); // Load tasks at startup

        System.out.println("_____________________________________________________");
        System.out.println("Hello! I'm FengWei");
        System.out.println("What can I do for you?");
        System.out.println("_____________________________________________________");

        // Exits the programme when the user types the command bye
        while (true) {
            String input = scanner.nextLine().trim();
            String[] inputParts = input.split(" ");

            if (inputParts.length == 0) {
                System.out.println("_____________________________________________________");
                System.out.println("Invalid command!");
                System.out.println("_____________________________________________________");
                continue;
            }

            String command = inputParts[0];

            if (command.equals("bye")) {
                break;
            }

            switch (command) {
            case "list":
                System.out.println("_____________________________________________________");
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskList.size(); i++) {
                    System.out.println((i + 1) + "." + taskList.get(i));
                }
                System.out.println("_____________________________________________________");
                // Finish the command
                continue;
            case "todo":
                String description = input.substring(5);
                Task t = new TodoTask(description);
                taskList.add(t);
                System.out.println("_____________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println(" " + t);
                System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                System.out.println("_____________________________________________________");
                continue;
            case "deadline":
                try {
                    String[] parts = input.split(" /by ", 2);
                    if (parts.length < 2) {
                        System.out.println("_____________________________________________________");
                        System.out.println(" OOPS!!! The deadline command must be in the format: deadline <description> /by <time>");
                        System.out.println("_____________________________________________________");
                        continue;
                    }
                    String deadlineDesc = parts[0].substring(9).trim();
                    String by = parts[1].trim();
                    Task d = new DeadlineTask(deadlineDesc, by); // If parsing date, handle inside DeadlineTask
                    taskList.add(d);
                    System.out.println("_____________________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + d);
                    System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                    System.out.println("_____________________________________________________");
                } catch (DateTimeParseException e) {
                    System.out.println("_____________________________________________________");
                    System.out.println("The deadline time format is invalid, use YYYY-MM-DD HHMM or a valid date");
                    System.out.println("_____________________________________________________");
                } catch (Exception e) {
                    System.out.println("_____________________________________________________");
                    System.out.println("Invalid deadline command format!");
                    System.out.println("_____________________________________________________");
                }
                continue;
            case "event":
                try {
                    String[] eventParts = input.split(" /from | /to ");
                    if (eventParts.length < 3) {
                        System.out.println("_____________________________________________________");
                        System.out.println(" OOPS!!! The event command must be in the format: event " +
                                "<description> /from <start> /to <end>");
                        System.out.println("_____________________________________________________");
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
                    System.out.println("_____________________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + e);
                    System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                    System.out.println("_____________________________________________________");
                } catch (DateTimeParseException e) {
                    System.out.println("_____________________________________________________");
                    System.out.println("The event time format is invalid, use YYYY-MM-DD HHMM or a valid date");
                    System.out.println("_____________________________________________________");
                } catch (Exception e) {
                    System.out.println("_____________________________________________________");
                    System.out.println("Invalid event command format!");
                    System.out.println("_____________________________________________________");
                }
                continue;
            case "mark":
                String[] taskMarks = input.split(" ");
                try {
                    if (taskMarks.length < 2) {
                        throw new FengWeiException("Please specify the task number to mark.");
                    }
                    // rest of the code
                } catch (FengWeiException e) {
                    System.out.println("_____________________________________________________");
                    System.out.println(" OOPS!!! " + e.getMessage());
                    System.out.println("_____________________________________________________");
                    continue;
                }

                try {
                    if (taskMarks.length > 2) {
                        throw new FengWeiException("Too many arguments");
                    }
                    // rest of the code
                } catch (FengWeiException e) {
                    System.out.println("_____________________________________________________");
                    System.out.println(" OOPS!!! " + e.getMessage());
                    System.out.println("_____________________________________________________");
                    continue;
                }

                int markNumber = Integer.parseInt(taskMarks[1]) - 1;
                taskList.get(markNumber).markAsDone();

                System.out.println("_____________________________________________________");
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("    " + taskList.get(markNumber));
                System.out.println("_____________________________________________________");
                break;
            case "unmark":
                String[] taskUnmarks = input.split(" ");
                int unmarkNumber = Integer.parseInt(taskUnmarks[1]) - 1;
                taskList.get(unmarkNumber).markAsNotDone();
                storage.saveTasks(taskList);
                System.out.println("_____________________________________________________");
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("    " + taskList.get(unmarkNumber));
                System.out.println("_____________________________________________________");
                storage.saveTasks(taskList);
                continue;
            case "delete":
                String[] parts = input.split(" ");
                int deleteNumber = Integer.parseInt(parts[1]) - 1;
                Task removedTask = taskList.remove(deleteNumber);
                System.out.println("_____________________________________________________");
                System.out.println("Noted. I've removed this task:");
                System.out.println(" " + removedTask);
                System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                System.out.println("_____________________________________________________");
                storage.saveTasks(taskList);
                continue;
            default:
                // Existing fallback code and exceptions
                System.out.println("_____________________________________________________");
                System.out.println("Invalid command!");
                System.out.println("_____________________________________________________");
                Task normal = new Task(input, ' ');
                taskList.add(normal);
                System.out.println("_____________________________________________________");
                System.out.println("added: " + input);
                System.out.println("_____________________________________________________");
            }
        }
        
        System.out.println("_____________________________________________________");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("_____________________________________________________");
        scanner.close();
    }
}
