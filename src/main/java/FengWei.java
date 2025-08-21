import java.util.Scanner;

public class FengWei {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("_____________________________________");
        System.out.println("Hello! I'm FengWei");
        System.out.println("What can I do for you?");
        System.out.println("_____________________________________");

        // Echo commands entered by the user, and exits when the user types the command bye
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }

            System.out.println("_____________________________________");
            System.out.println(input);
            System.out.println("_____________________________________");
        }

        System.out.println("_____________________________________");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("_____________________________________");
        scanner.close();
    }
}
