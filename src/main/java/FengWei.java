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
                for (int i = 0; i < taskListIndex; i++) {
                    Task task = taskList[i];
                    System.out.println((i + 1) + ". [" + task.getStatusIcon() + "] " + task.getDescription());
                }
                System.out.println("_____________________________________________________");
                // Finish the command
                continue;
            }

            // Mark task as done if user types "mark" followed by task number
            if (input.startsWith("mark")) {
                String[] taskMarks = input.split(" ");
                int taskNumber  = Integer.parseInt(taskMarks[1]) - 1;
                taskList[taskNumber].markAsDone();

                System.out.println("_____________________________________________________");
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("    [" + taskList[taskNumber].getStatusIcon() + "] "
                        + taskList[taskNumber].getDescription());
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
                System.out.println("    [" + taskList[taskNumber].getStatusIcon() + "] "
                        + taskList[taskNumber].getDescription());
                System.out.println("_____________________________________________________");
                continue;
            }
            // Since the input is not the list command, add task to task list
            // Assign input to array
            taskList[taskListIndex] = new Task(input);
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
