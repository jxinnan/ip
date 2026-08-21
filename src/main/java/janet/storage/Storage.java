package janet.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import janet.task.Deadline;
import janet.task.Event;
import janet.task.Task;
import janet.task.TaskList;
import janet.task.Todo;

/**
 * Loads Janet's tasks from disk and saves task changes to disk.
 */
public class Storage {
    /** Relative path of the file that stores Janet's tasks. */
    private final Path dataFilePath;

    /**
     * Creates storage for one task data file.
     *
     * @param filePath relative path of the data file
     */
    public Storage(String filePath) {
        dataFilePath = Path.of(filePath);
    }

    /**
     * Loads all valid tasks from the data file.
     *
     * @return loaded tasks, or an empty list when no data file exists
     */
    public List<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        if (Files.notExists(dataFilePath)) {
            return tasks;
        }

        try {
            for (String line : Files.readAllLines(dataFilePath, StandardCharsets.UTF_8)) {
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
     * Saves all tasks to the data file.
     *
     * @param tasks tasks to persist
     */
    public void save(TaskList tasks) {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks.getTasks()) {
            lines.add(formatStoredTask(task));
        }

        try {
            Path parentPath = dataFilePath.getParent();
            if (parentPath != null) {
                Files.createDirectories(parentPath);
            }
            Files.write(dataFilePath, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            System.err.println("Unable to save tasks: " + exception.getMessage());
        }
    }

    /**
     * Creates a task from one tab-separated line in the data file.
     *
     * @param line a line read from the data file
     * @return a reconstructed task, or {@code null} when the line is malformed
     */
    private Task parseStoredTask(String line) {
        String[] parts = line.split("\\t", -1);
        if (parts.length < 3) {
            reportMalformedTask(line);
            return null;
        }

        try {
            Task task;
            if (parts[0].equals("T") && parts.length == 3) {
                task = new Todo(parts[2]);
            } else if (parts[0].equals("D") && parts.length == 4) {
                task = new Deadline(parts[2], LocalDate.parse(parts[3]));
            } else if (parts[0].equals("E") && parts.length == 5) {
                task = new Event(parts[2], parts[3], parts[4]);
            } else {
                reportMalformedTask(line);
                return null;
            }

            if (parts[1].equals("1")) {
                task.markAsDone();
            } else if (!parts[1].equals("0")) {
                reportMalformedTask(line);
                return null;
            }
            return task;
        } catch (DateTimeParseException exception) {
            reportMalformedTask(line);
            return null;
        }
    }

    /**
     * Converts one task to a tab-separated data-file line.
     *
     * @param task task to persist
     * @return a data-file line representing the task
     */
    private String formatStoredTask(Task task) {
        String completionStatus = task.isDone() ? "1" : "0";
        if (task instanceof Todo) {
            return String.join("\t", "T", completionStatus, task.getRawDescription());
        }
        if (task instanceof Deadline deadline) {
            return String.join("\t", "D", completionStatus, task.getRawDescription(),
                    deadline.getDeadline().toString());
        }
        if (task instanceof Event event) {
            return String.join("\t", "E", completionStatus, task.getRawDescription(), event.getStart(),
                    event.getEnd());
        }
        throw new IllegalArgumentException("Cannot save an unknown task type.");
    }

    /**
     * Reports a malformed saved task without preventing the remaining tasks from loading.
     *
     * @param line malformed data-file line
     */
    private void reportMalformedTask(String line) {
        System.err.println("Ignoring malformed saved task: " + line);
    }
}
