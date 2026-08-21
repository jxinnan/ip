package janet.ui;

import java.util.List;
import java.util.Scanner;

import janet.task.Task;

/**
 * Handles Janet's console input and output.
 */
public class Ui {
    /** Divider printed between Janet's responses. */
    private static final String DIVIDER = "____________________________________________________________";

    /** Reads commands entered through the console. */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Shows Janet's welcome banner.
     */
    public void showWelcome() {
        System.out.println(DIVIDER);
        System.out.println("     _                  _");
        System.out.println("    | |                | |");
        System.out.println("    | | __ _ _ __   ___| |_");
        System.out.println(" _  | |/ _` | '_ \\ / _ \\ __|");
        System.out.println("| |_| | (_| | | | |  __/ |_");
        System.out.println(" \\___/ \\__,_|_| |_|\\___|\\__|");
        System.out.println(DIVIDER);
        System.out.println("Hi! I'm Janet! I'm here to help with absolutely anything.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    /**
     * Returns whether another command is available from standard input.
     *
     * @return whether a command can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads one complete command from standard input.
     *
     * @return the user's command
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows the list of tasks.
     *
     * @param tasks tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println(" Here are the tasks in your list:");
        for (int index = 0; index < tasks.size(); index++) {
            Task task = tasks.get(index);
            System.out.println(" " + (index + 1) + "." + task.getTypeIcon()
                    + "[" + task.getStatusIcon() + "] " + task.getDescription());
        }
    }

    /**
     * Shows confirmation that a task has been added.
     *
     * @param task the task added
     * @param taskCount total number of tasks
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task.getTypeIcon() + "[ ] " + task.getDescription());
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows confirmation that a task has been marked as done.
     *
     * @param task the completed task
     */
    public void showTaskMarked(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        showTask(task);
    }

    /**
     * Shows confirmation that a task has been marked as incomplete.
     *
     * @param task the incomplete task
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(" Okay, I've marked this task as not done yet:");
        showTask(task);
    }

    /**
     * Shows confirmation that a task has been removed.
     *
     * @param task the removed task
     * @param taskCount total number of tasks remaining
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println(" Noted. I've removed this task:");
        showTask(task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows a user-facing error message.
     *
     * @param message the error message
     */
    public void showError(String message) {
        System.out.println(" " + message);
    }

    /**
     * Shows Janet's goodbye message.
     */
    public void showGoodbye() {
        System.out.println(DIVIDER);
        System.out.println(" Okay! Have a wonderful day. Bye!");
    }

    /**
     * Shows the standard divider line.
     */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Shows one task in Janet's indented response format.
     *
     * @param task the task to display
     */
    private void showTask(Task task) {
        System.out.println("   " + task.getTypeIcon() + "[" + task.getStatusIcon() + "] "
                + task.getDescription());
    }
}
