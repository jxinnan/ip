import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A simple command-line chatbot that echoes user input until asked to leave.
 */
public class Janet {
    /** Maximum number of tasks Janet can store during one run. */
    private static final int MAX_TASKS = Integer.MAX_VALUE;

    /** Relative path used to persist Janet's tasks between runs. */
    private static final Path DATA_FILE_PATH = Path.of("data", "janet.txt");

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

        ArrayList<Task> tasks = loadTasks();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            CommandName commandName = CommandName.from(command);

            if (commandName == CommandName.BYE) {
                System.out.println("____________________________________________________________");
                System.out.println(" Okay! Have a wonderful day. Bye!");
                System.out.println("____________________________________________________________");
                scanner.close();
                break;
            }

            try {
            if (commandName == CommandName.LIST) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(" " + (i + 1) + "." + tasks.get(i).getTypeIcon()
                            + "[" + tasks.get(i).getStatusIcon() + "] "
                            + tasks.get(i).getDescription());
                }
            } else if (commandName == CommandName.TODO) {
                String description = command.length() > 5 ? command.substring(5).trim() : "";
                if (description.isEmpty()) {
                    throw new InvalidCommandException("OOPS!!! A todo needs a description.");
                }
                addTask(new Todo(description), tasks);
            } else if (commandName == CommandName.EVENT) {
                String eventCommand = command.length() > 6 ? command.substring(6).trim() : "";
                int fromIndex = eventCommand.indexOf(" /from ");
                int toIndex = eventCommand.indexOf(" /to ");
                if (fromIndex > 0 && toIndex > fromIndex) {
                    String description = eventCommand.substring(0, fromIndex).trim();
                    int startIndex = fromIndex + 7;
                    String start = toIndex >= startIndex
                            ? eventCommand.substring(startIndex, toIndex).trim()
                            : "";
                    String end = eventCommand.substring(toIndex + 5).trim();
                    if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
                        throw new InvalidCommandException("OOPS!!! An event needs a description, start, and end.");
                    }
                    addTask(new Event(description, start, end), tasks);
                } else {
                    throw new InvalidCommandException(
                            "Sorry, please use: event <task> /from <start> /to <end>.");
                }
            } else if (commandName == CommandName.DEADLINE) {
                String deadlineCommand = command.length() > 9 ? command.substring(9).trim() : "";
                int byIndex = deadlineCommand.indexOf(" /by ");
                if (byIndex > 0) {
                    String description = deadlineCommand.substring(0, byIndex).trim();
                    String deadline = deadlineCommand.substring(byIndex + 5).trim();
                    if (description.isEmpty() || deadline.isEmpty()) {
                        throw new InvalidCommandException("OOPS!!! A deadline needs a description and due time.");
                    }
                    addTask(new Deadline(description, deadline), tasks);
                } else {
                    throw new InvalidCommandException(
                            "Sorry, please use: deadline <task> /by <date or time>.");
                }
            } else if (commandName == CommandName.DELETE) {
                deleteTask(command, tasks);
            } else if (commandName == CommandName.MARK) {
                markTask(command, tasks);
            } else if (commandName == CommandName.UNMARK) {
                unmarkTask(command, tasks);
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
     */
    private static void addTask(Task task, ArrayList<Task> tasks) {
        if (tasks.size() >= MAX_TASKS) {
            throw new TaskLimitException("Sorry, I can only store " + MAX_TASKS + " tasks.");
        }

        tasks.add(task);
        saveTasks(tasks);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task.getTypeIcon() + "[ ] " + task.getDescription());
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Marks the task selected by a {@code mark <number>} command as done.
     *
     * @param command the complete command entered by the user
     * @param tasks the stored tasks
     */
    private static void markTask(String command, ArrayList<Task> tasks) {
        Task task = getTask(command, 5, tasks);
        if (task == null) {
            return;
        }

        task.markAsDone();
        saveTasks(tasks);
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task.getTypeIcon() + "[" + task.getStatusIcon() + "] "
                + task.getDescription());
    }

    /**
     * Marks the task selected by an {@code unmark <number>} command as not done.
     *
     * @param command the complete command entered by the user
     * @param tasks the stored tasks
     */
    private static void unmarkTask(String command, ArrayList<Task> tasks) {
        Task task = getTask(command, 7, tasks);
        if (task == null) {
            return;
        }

        task.markAsUndone();
        saveTasks(tasks);
        System.out.println(" Okay, I've marked this task as not done yet:");
        System.out.println("   " + task.getTypeIcon() + "[" + task.getStatusIcon() + "] "
                + task.getDescription());
    }

    /**
     * Removes the task selected by a {@code delete <number>} command.
     *
     * @param command the complete command entered by the user
     * @param tasks the stored tasks
     */
    private static void deleteTask(String command, ArrayList<Task> tasks) {
        Task task = getTask(command, 7, tasks);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task.getTypeIcon() + "[" + task.getStatusIcon() + "] "
                + task.getDescription());
        tasks.remove(task);
        saveTasks(tasks);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Finds the task selected by a command and validates its task number.
     *
     * @param command the complete command entered by the user
     * @param numberStart the index where the task number begins
     * @param tasks the stored tasks
     * @return the selected task, or {@code null} if the command is invalid
     */
    private static Task getTask(String command, int numberStart, ArrayList<Task> tasks) {
        if (command.length() <= numberStart) {
            throw new InvalidTaskException("Sorry, please provide a valid task number.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(command.substring(numberStart).trim());
        } catch (NumberFormatException exception) {
            throw new InvalidTaskException("Sorry, please provide a valid task number.");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new InvalidTaskException("Sorry, that task number does not exist.");
        }

        return tasks.get(taskNumber - 1);
    }

    /**
     * Loads tasks from the data file, returning an empty list when it has not been created yet.
     *
     * @return the tasks stored in the data file
     */
    private static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (Files.notExists(DATA_FILE_PATH)) {
            return tasks;
        }

        try {
            for (String line : Files.readAllLines(DATA_FILE_PATH, StandardCharsets.UTF_8)) {
                Task task = parseStoredTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException exception) {
            System.err.println("Unable to load saved tasks: " + exception.getMessage());
        }
        return tasks;
    }

    /**
     * Creates a task from one tab-separated line in Janet's data file.
     *
     * @param line a line read from the data file
     * @return the reconstructed task, or {@code null} for a malformed line
     */
    private static Task parseStoredTask(String line) {
        String[] parts = line.split("\\t", -1);
        if (parts.length < 3) {
            System.err.println("Ignoring malformed saved task: " + line);
            return null;
        }

        Task task;
        if (parts[0].equals("T") && parts.length == 3) {
            task = new Todo(parts[2]);
        } else if (parts[0].equals("D") && parts.length == 4) {
            task = new Deadline(parts[2], parts[3]);
        } else if (parts[0].equals("E") && parts.length == 5) {
            task = new Event(parts[2], parts[3], parts[4]);
        } else {
            System.err.println("Ignoring malformed saved task: " + line);
            return null;
        }

        if (parts[1].equals("1")) {
            task.markAsDone();
        } else if (!parts[1].equals("0")) {
            System.err.println("Ignoring malformed saved task: " + line);
            return null;
        }
        return task;
    }

    /**
     * Writes every task to the data file, creating its parent directory when needed.
     *
     * @param tasks the tasks to persist
     */
    private static void saveTasks(List<Task> tasks) {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(formatStoredTask(task));
        }

        try {
            Files.createDirectories(DATA_FILE_PATH.getParent());
            Files.write(DATA_FILE_PATH, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.err.println("Unable to save tasks: " + exception.getMessage());
        }
    }

    /**
     * Converts one task to a tab-separated line for Janet's data file.
     *
     * @param task the task to persist
     * @return one data-file line representing the task
     */
    private static String formatStoredTask(Task task) {
        String completionStatus = task.isDone ? "1" : "0";
        if (task instanceof Todo) {
            return String.join("\t", "T", completionStatus, task.description);
        }
        if (task instanceof Deadline deadline) {
            return String.join("\t", "D", completionStatus, task.description, deadline.getDeadline());
        }
        if (task instanceof Event event) {
            return String.join("\t", "E", completionStatus, task.description, event.getStart(), event.getEnd());
        }
        throw new IllegalArgumentException("Cannot save an unknown task type.");
    }
}
