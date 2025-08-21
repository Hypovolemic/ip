import java.util.Scanner;

public class FengWei {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] taskList = new Task[100];
        int taskListIndex = 0;

        System.out.println("_____________________________________________________");
        System.out.println("Hello! I'm FengWei");
        System.out.println("What can I do for you?");
        System.out.println("_____________________________________________________");

        // Exits the programme when the user types the command bye
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }

            // Checks if user types "list"
            if (input.equals("list")) {
                System.out.println("_____________________________________________________");
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskListIndex; i++) {
                    Task task = taskList[i];
                    System.out.println((i + 1) + "." + task.toString());
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
                taskList[taskNumber].markAsDone();

                System.out.println("_____________________________________________________");
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("    " + taskList[taskNumber].toString());
                System.out.println("_____________________________________________________");
                continue;
            }

            // Mark task as undone if user types "unmark" followed by task number
            if (input.startsWith("unmark")) {
                String[] taskMarks = input.split(" ");
                int taskNumber  = Integer.parseInt(taskMarks[1]) - 1;
                taskList[taskNumber].markAsNotDone();

                System.out.println("_____________________________________________________");
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("    " + taskList[taskNumber].toString());
                System.out.println("_____________________________________________________");
                continue;
            }

            if (input.startsWith("todo")) {
                String description = input.substring(5);
                Task t = new TodoTask(description);
                taskList[taskListIndex++] = t;
                System.out.println("_____________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t);
                System.out.println("Now you have " + taskListIndex + " tasks in the list.");
                System.out.println("_____________________________________________________");
                continue;
            }

            if (input.startsWith("deadline")) {
                String[] parts = input.split(" /by ");
                String description = parts[0].substring(9);
                String by = parts[1];
                Task d = new DeadlineTask(description, by);
                taskList[taskListIndex++] = d;
                System.out.println("_____________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + d);
                System.out.println("Now you have " + taskListIndex + " tasks in the list.");
                System.out.println("_____________________________________________________");
                continue;
            }

            if (input.startsWith("event")) {
                String[] parts = input.split(" /from | /to ");
                String description = parts[0].substring(6); // remove "event "
                String from = parts[1];
                String to = parts[2];

                Task e = new EventTask(description, from, to);
                taskList[taskListIndex++] = e;

                System.out.println("_____________________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + e);
                System.out.println("Now you have " + taskListIndex + " tasks in the list.");
                System.out.println("_____________________________________________________");
                continue;
            }

            // Handle unknown commands
            System.out.println("_____________________________________________________");
            System.out.println("Invalid command!");
            System.out.println("_____________________________________________________");


            // Since the input is not the list command, add task to task list
            // Assign input to array
            taskList[taskListIndex] = new Task(input, ' ');
            taskListIndex++;

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
