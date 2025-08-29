import java.util.Scanner;
import java.util.ArrayList;

public class FengWei {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> taskList = new ArrayList<>();

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
                    String[] parts = inputParts[1].split(" /by ");
                    String deadlineDesc = parts[0].substring(9);
                    String by = parts[1];
                    Task d = new DeadlineTask(deadlineDesc, by);
                    taskList.add(d);
                    System.out.println("_____________________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + d);
                    System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                    System.out.println("_____________________________________________________");
                    continue;
                } catch (Exception e) {
                    System.out.println("_____________________________________________________");
                    System.out.println("Invalid deadline command format!");
                    System.out.println("_____________________________________________________");
                }
                continue;
            case "event":
                try {
                    // Example: event project meeting /from Aug 6th 2pm /to Aug 6th 4pm
                    String[] eventParts = input.split(" /from | /to ");
                    String eventDesc = eventParts[0].substring(6); // remove "event "
                    String from = eventParts[1];
                    String to = eventParts[2];

                    Task e = new EventTask(eventDesc, from, to);
                    taskList.add(e);

                    System.out.println("_____________________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + e);
                    System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                    System.out.println("_____________________________________________________");
                    continue;
                }
                catch (Exception e){
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

                System.out.println("_____________________________________________________");
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("    " + taskList.get(unmarkNumber));
                System.out.println("_____________________________________________________");
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
