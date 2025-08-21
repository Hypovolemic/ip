import java.util.Scanner;

public class FengWei {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] taskList = new String[100];
        int taskListIndex = 0;

        System.out.println("_____________________________________");
        System.out.println("Hello! I'm FengWei");
        System.out.println("What can I do for you?");
        System.out.println("_____________________________________");

        // Exits the programme when the user types the command bye
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }

            // Checks if user types "list"
            if (input.equals("list")) {
                System.out.println("_____________________________________");
                for (int i = 0; i < taskList.length; i++) {
                    if (taskList[i] != null) {
                        System.out.println(i + 1 + ". " + taskList[i]);
                    }
                }
                System.out.println("_____________________________________");
                // Finish the command
                continue;
            }

            // Since the input is not the list command, add task to task list
            // Assign input to array
            taskList[taskListIndex] = input;
            taskListIndex++;

            // Echo added task
            System.out.println("_____________________________________");
            System.out.println("added: " + input);
            System.out.println("_____________________________________");
        }

        System.out.println("_____________________________________");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("_____________________________________");
        scanner.close();
    }
}
