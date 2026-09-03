package janet.ui;

import java.io.PrintStream;
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

    /** Receives Janet's displayed messages. */
    private final PrintStream output;

    /**
     * Creates a console user interface.
     */
    public Ui() {
        this(System.out);
    }

    /**
     * Creates a user interface that writes to a specific output stream.
     *
     * @param output destination for Janet's messages
     */
    public Ui(PrintStream output) {
        this.output = output;
    }

    /**
     * Shows Janet's welcome banner.
     */
    public void showWelcome() {
        output.println(DIVIDER);
        output.println("     _                  _");
        output.println("    | |                | |");
        output.println("    | | __ _ _ __   ___| |_");
        output.println(" _  | |/ _` | '_ \\ / _ \\ __|");
        output.println("| |_| | (_| | | | |  __/ |_");
        output.println(" \\___/ \\__,_|_| |_|\\___|\\__|");
        output.println(DIVIDER);
        output.println("Hi! I'm Janet! I'm here to help with absolutely anything.");
        output.println("What can I do for you?");
        output.println(DIVIDER);
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
        output.println(" Here are the tasks in your list:");
        showTasks(tasks);
    }

    /**
     * Shows tasks matching a user-entered keyword.
     *
     * @param tasks matching tasks to display
     */
    public void showMatchingTasks(List<Task> tasks) {
        output.println(" Here are the matching tasks in your list:");
        showTasks(tasks);
    }

    /**
     * Shows tasks as a numbered list.
     *
     * @param tasks tasks to display
     */
    private void showTasks(List<Task> tasks) {
        for (int index = 0; index < tasks.size(); index++) {
            Task task = tasks.get(index);
            output.println(" " + (index + 1) + "." + task.getTypeIcon()
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
        output.println(" Got it. I've added this task:");
        output.println("   " + task.getTypeIcon() + "[ ] " + task.getDescription());
        output.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows confirmation that a task has been marked as done.
     *
     * @param task the completed task
     */
    public void showTaskMarked(Task task) {
        output.println(" Nice! I've marked this task as done:");
        showTask(task);
    }

    /**
     * Shows confirmation that a task has been marked as incomplete.
     *
     * @param task the incomplete task
     */
    public void showTaskUnmarked(Task task) {
        output.println(" Okay, I've marked this task as not done yet:");
        showTask(task);
    }

    /**
     * Shows confirmation that a task has been removed.
     *
     * @param task the removed task
     * @param taskCount total number of tasks remaining
     */
    public void showTaskDeleted(Task task, int taskCount) {
        output.println(" Noted. I've removed this task:");
        showTask(task);
        output.println(" Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows a user-facing error message.
     *
     * @param message the error message
     */
    public void showError(String message) {
        output.println(" " + message);
    }

    /**
     * Shows Janet's goodbye message.
     */
    public void showGoodbye() {
        output.println(DIVIDER);
        output.println(" Okay! Have a wonderful day. Bye!");
    }

    /**
     * Shows the standard divider line.
     */
    public void showLine() {
        output.println(DIVIDER);
    }

    /**
     * Shows one task in Janet's indented response format.
     *
     * @param task the task to display
     */
    private void showTask(Task task) {
        output.println("   " + task.getTypeIcon() + "[" + task.getStatusIcon() + "] "
                + task.getDescription());
    }
}
