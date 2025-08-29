import java.util.List;
import java.util.Scanner;

public class FengWei {
    public static void main(String[] args) {
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
            if (input.equals("bye")) {
                break;
            }

            // Checks if user types "list"
            if (input.equals("list")) {
                System.out.println("_____________________________________________________");
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskList.size(); i++) {
                    System.out.println((i + 1) + "." + taskList.get(i));
                }
                System.out.println("_____________________________________________________");
                // Finish the command
                continue;
            }

            // Mark task as done if user types "mark" followed by task number
            if (input.startsWith("mark")) {
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

                int taskNumber  = Integer.parseInt(taskMarks[1]) - 1;
                taskList.get(taskNumber).markAsDone();

                System.out.println("_____________________________________________________");
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("    " + taskList.get(taskNumber));
                System.out.println("_____________________________________________________");
                storage.saveTasks(taskList);
                continue;
            }

            // Mark task as undone if user types "unmark" followed by task number
            if (input.startsWith("unmark")) {
                String[] taskMarks = input.split(" ");
                int taskNumber  = Integer.parseInt(taskMarks[1]) - 1;
                taskList.get(taskNumber).markAsNotDone();

                System.out.println("_____________________________________________________");
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("    " + taskList.get(taskNumber));
                System.out.println("_____________________________________________________");
                storage.saveTasks(taskList);
                continue;
            }

            if (input.startsWith("todo")) {
                String description = input.substring(5);
                Task t = new TodoTask(description);
                taskList.add(t);
                System.out.println("_____________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t);
                System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                System.out.println("_____________________________________________________");
                storage.saveTasks(taskList);
                continue;
            }

            if (input.startsWith("deadline")) {
                String[] parts = input.split(" /by ");
                String description = parts[0].substring(9);
                String by = parts[1];
                Task d = new DeadlineTask(description, by);
                taskList.add(d);
                System.out.println("_____________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + d);
                System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                System.out.println("_____________________________________________________");
                storage.saveTasks(taskList);
                continue;
            }

            if (input.startsWith("event")) {
                String[] parts = input.split(" /from | /to ");
                String description = parts[0].substring(6); // remove "event "
                String from = parts[1];
                String to = parts[2];

                Task e = new EventTask(description, from, to);
                taskList.add(e);

                System.out.println("_____________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + e);
                System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                System.out.println("_____________________________________________________");
                storage.saveTasks(taskList);
                continue;
            }

            if (input.startsWith("delete")) {
                String[] parts = input.split(" ");
                int taskNumber = Integer.parseInt(parts[1]) - 1;
                Task removedTask = taskList.remove(taskNumber);
                System.out.println("_____________________________________________________");
                System.out.println("Noted. I've removed this task:");
                System.out.println(" " + removedTask);
                System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                System.out.println("_____________________________________________________");
                storage.saveTasks(taskList);
                continue;
            }

            // Handle unknown commands
            System.out.println("_____________________________________________________");
            System.out.println("Invalid command!");
            System.out.println("_____________________________________________________");


            // Since the input is not the list command, add task to task list
            // Assign input to array
            Task normal = new Task(input, ' ');
            taskList.add(normal);
            storage.saveTasks(taskList); // Save tasks after each modification

            // Echo added task
            System.out.println("_____________________________________________________");
            System.out.println("added: " + input);
            System.out.println("_____________________________________________________");
        }

        System.out.println("_____________________________________________________");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("_____________________________________________________");
        scanner.close();
    }
}
