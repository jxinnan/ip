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

        Task[] tasks = new Task[MAX_TASKS];
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
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i].getTypeIcon()
                            + "[" + tasks[i].getStatusIcon() + "] "
                            + tasks[i].getDescription());
                }
            } else if (command.startsWith("todo ")) {
                if (taskCount < MAX_TASKS) {
                    tasks[taskCount] = new Todo(command.substring(5).trim());
                    taskCount++;
                    System.out.println(" Got it. I've added this task:");
                    System.out.println("   [T][ ] " + tasks[taskCount - 1].getDescription());
                    System.out.println(" Now you have " + taskCount + " tasks in the list.");
                } else {
                    System.out.println(" Sorry, I can only store " + MAX_TASKS + " tasks.");
                }
            } else if (command.startsWith("deadline ")) {
                if (taskCount < MAX_TASKS) {
                    String deadlineCommand = command.substring(9).trim();
                    int byIndex = deadlineCommand.indexOf(" /by ");
                    if (byIndex > 0) {
                        String description = deadlineCommand.substring(0, byIndex).trim();
                        String deadline = deadlineCommand.substring(byIndex + 5).trim();
                        tasks[taskCount] = new Deadline(description, deadline);
                        taskCount++;
                        Task task = tasks[taskCount - 1];
                        System.out.println(" Got it. I've added this task:");
                        System.out.println("   " + task.getTypeIcon() + "[ ] " + task.getDescription());
                        System.out.println(" Now you have " + taskCount + " tasks in the list.");
                    } else {
                        System.out.println(" Sorry, please use: deadline <task> /by <date or time>.");
                    }
                } else {
                    System.out.println(" Sorry, I can only store " + MAX_TASKS + " tasks.");
                }
            } else if (command.startsWith("mark ")) {
                markTask(command, tasks, taskCount);
            } else if (command.startsWith("unmark ")) {
                unmarkTask(command, tasks, taskCount);
            } else if (taskCount < MAX_TASKS) {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println(" added: " + command);
            } else {
                System.out.println(" Sorry, I can only store " + MAX_TASKS + " tasks.");
            }

            System.out.println("____________________________________________________________");
        }
    }

    /**
     * Marks the task selected by a {@code mark <number>} command as done.
     *
     * @param command the complete command entered by the user
     * @param tasks the stored tasks
     * @param taskCount the number of stored tasks
     */
    private static void markTask(String command, Task[] tasks, int taskCount) {
        Task task = getTask(command, 5, tasks, taskCount);
        if (task == null) {
            return;
        }

        task.markAsDone();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task.getTypeIcon() + "[" + task.getStatusIcon() + "] "
                + task.getDescription());
    }

    /**
     * Marks the task selected by an {@code unmark <number>} command as not done.
     *
     * @param command the complete command entered by the user
     * @param tasks the stored tasks
     * @param taskCount the number of stored tasks
     */
    private static void unmarkTask(String command, Task[] tasks, int taskCount) {
        Task task = getTask(command, 7, tasks, taskCount);
        if (task == null) {
            return;
        }

        task.markAsUndone();
        System.out.println(" Okay, I've marked this task as not done yet:");
        System.out.println("   " + task.getTypeIcon() + "[" + task.getStatusIcon() + "] "
                + task.getDescription());
    }

    /**
     * Finds the task selected by a command and validates its task number.
     *
     * @param command the complete command entered by the user
     * @param numberStart the index where the task number begins
     * @param tasks the stored tasks
     * @param taskCount the number of stored tasks
     * @return the selected task, or {@code null} if the command is invalid
     */
    private static Task getTask(String command, int numberStart, Task[] tasks, int taskCount) {
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(command.substring(numberStart).trim());
        } catch (NumberFormatException exception) {
            System.out.println(" Sorry, please provide a valid task number.");
            return null;
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            System.out.println(" Sorry, that task number does not exist.");
            return null;
        }

        return tasks[taskNumber - 1];
    }
}
