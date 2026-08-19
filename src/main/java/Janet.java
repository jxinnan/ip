import java.util.Scanner;

/**
 * A simple command-line chatbot that echoes user input until asked to leave.
 */
public class Janet {
    /** Maximum number of tasks Janet can store during one run. */
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        String banner = "____________________________________________________________\n"
                + "     _                  _\n"
                + "    | |                | |\n"
                + "    | | __ _ _ __   ___| |_\n"
                + " _  | |/ _` | '_ \\ / _ \\ __|\n"
                + "| |_| | (_| | | | |  __/ |_\n"
                + " \\___/ \\__,_|_| |_|\\___|\\__|\n"
                + "____________________________________________________________\n"
                + "Hi! I'm Janet! I'm here to help with absolutely anything.\n"
                + "What can I do for you?\n"
                + "____________________________________________________________\n";
        System.out.print(banner);

        String[] tasks = new String[MAX_TASKS];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println(" Okay! Have a wonderful day. Bye!");
                System.out.println("____________________________________________________________");
                scanner.close();
                break;
            }

            if (command.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
                }
            } else if (taskCount < MAX_TASKS) {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println(" added: " + command);
            } else {
                System.out.println(" Sorry, I can only store " + MAX_TASKS + " tasks.");
            }

            System.out.println("____________________________________________________________");
        }
    }
}
