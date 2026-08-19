import java.util.Scanner;

/**
 * A simple command-line chatbot that echoes user input until asked to leave.
 */
public class Janet {
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

            System.out.println(" " + command);
            System.out.println("____________________________________________________________");
        }
    }
}
