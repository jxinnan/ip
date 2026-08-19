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

            try {
            if (command.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i].getTypeIcon()
                            + "[" + tasks[i].getStatusIcon() + "] "
                            + tasks[i].getDescription());
                }
            } else if (command.equals("todo") || command.startsWith("todo ")) {
                String description = command.length() > 5 ? command.substring(5).trim() : "";
                if (description.isEmpty()) {
                    throw new InvalidCommandException("OOPS!!! A todo needs a description.");
                }
                taskCount = addTask(new Todo(description), tasks, taskCount);
            } else if (command.equals("event") || command.startsWith("event ")) {
                String eventCommand = command.length() > 6 ? command.substring(6).trim() : "";
                int fromIndex = eventCommand.indexOf(" /from ");
                int toIndex = eventCommand.indexOf(" /to ");
                if (fromIndex > 0 && toIndex > fromIndex) {
                    String description = eventCommand.substring(0, fromIndex).trim();
                    String start = eventCommand.substring(fromIndex + 7, toIndex).trim();
                    String end = eventCommand.substring(toIndex + 5).trim();
                    if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
                        throw new InvalidCommandException("OOPS!!! An event needs a description, start, and end.");
                    }
                    taskCount = addTask(new Event(description, start, end), tasks, taskCount);
                } else {
                    throw new InvalidCommandException(
                            "Sorry, please use: event <task> /from <start> /to <end>.");
                }
            } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                String deadlineCommand = command.length() > 9 ? command.substring(9).trim() : "";
                int byIndex = deadlineCommand.indexOf(" /by ");
                if (byIndex > 0) {
                    String description = deadlineCommand.substring(0, byIndex).trim();
                    String deadline = deadlineCommand.substring(byIndex + 5).trim();
                    if (description.isEmpty() || deadline.isEmpty()) {
                        throw new InvalidCommandException("OOPS!!! A deadline needs a description and due time.");
                    }
                    taskCount = addTask(new Deadline(description, deadline), tasks, taskCount);
                } else {
                    throw new InvalidCommandException(
                            "Sorry, please use: deadline <task> /by <date or time>.");
                }
            } else if (command.startsWith("mark ")) {
                markTask(command, tasks, taskCount);
            } else if (command.startsWith("unmark ")) {
                unmarkTask(command, tasks, taskCount);
            } else {
                throw new InvalidCommandException("OOPS!!! I don't recognize that command.");
            }
            } catch (JanetException exception) {
                System.out.println(" " + exception.getMessage());
            }

            System.out.println("____________________________________________________________");
        }
    }

    /**
     * Adds a task to the list and prints the appropriate confirmation.
     *
     * @param task the task to add
     * @param tasks the stored tasks
     * @param taskCount the current number of tasks
     * @return the updated number of tasks
     */
    private static int addTask(Task task, Task[] tasks, int taskCount) {
        if (taskCount >= MAX_TASKS) {
            throw new TaskLimitException("Sorry, I can only store " + MAX_TASKS + " tasks.");
        }

        tasks[taskCount] = task;
        taskCount++;
        if (task instanceof Todo || task instanceof Deadline || task instanceof Event) {
            System.out.println(" Got it. I've added this task:");
            System.out.println("   " + task.getTypeIcon() + "[ ] " + task.getDescription());
            System.out.println(" Now you have " + taskCount + " tasks in the list.");
        } else {
            System.out.println(" added: " + task.getDescription());
        }
        return taskCount;
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
            throw new InvalidTaskException("Sorry, please provide a valid task number.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new InvalidTaskException("Sorry, that task number does not exist.");
        }

        return tasks[taskNumber - 1];
    }
}
