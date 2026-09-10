package janet.task;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import janet.exception.InvalidTaskException;
import janet.exception.TaskLimitException;

/**
 * Stores and manages Janet's tasks.
 */
public class TaskList {
    /** Maximum number of tasks Janet can store. */
    private static final int MAX_TASKS = Integer.MAX_VALUE;

    /** Tasks in their display order. */
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this(new ArrayList<>());
    }

    /**
     * Creates a task list containing loaded tasks.
     *
     * @param tasks tasks to place in the new list
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to this list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        assert task != null : "Only tasks created or loaded by Janet may be added";

        if (tasks.size() >= MAX_TASKS) {
            throw new TaskLimitException("Sorry, I can only store " + MAX_TASKS + " tasks.");
        }
        tasks.add(task);
    }

    /**
     * Returns a task by its one-based task number.
     *
     * @param taskNumber one-based task number
     * @return the selected task
     */
    public Task get(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new InvalidTaskException("Sorry, that task number does not exist.");
        }
        return tasks.get(taskNumber - 1);
    }

    /**
     * Removes and returns a task by its one-based task number.
     *
     * @param taskNumber one-based task number
     * @return the removed task
     */
    public Task delete(int taskNumber) {
        return delete(List.of(taskNumber)).get(0);
    }

    /**
     * Removes and returns tasks by their one-based task numbers.
     *
     * <p>All task numbers are validated before any task is removed. Returned tasks follow the order of the
     * supplied task numbers.</p>
     *
     * @param taskNumbers one-based task numbers
     * @return the removed tasks in the requested order
     */
    public List<Task> delete(List<Integer> taskNumbers) {
        if (taskNumbers.isEmpty()) {
            throw new InvalidTaskException("Sorry, please provide a valid task number.");
        }
        if (new HashSet<>(taskNumbers).size() != taskNumbers.size()) {
            throw new InvalidTaskException("Sorry, please provide each task number only once.");
        }

        List<Task> deletedTasks = taskNumbers.stream()
                .map(this::get)
                .toList();
        List<Integer> descendingTaskNumbers = new ArrayList<>(taskNumbers);
        descendingTaskNumbers.sort(Comparator.reverseOrder());
        for (int taskNumber : descendingTaskNumbers) {
            tasks.remove(taskNumber - 1);
        }
        return deletedTasks;
    }

    /**
     * Returns tasks whose unformatted descriptions contain a keyword.
     *
     * @param keyword text to search for
     * @return matching tasks in their original order
     */
    public List<Task> find(String keyword) {
        return tasks.stream()
                .filter(task -> task.getRawDescription().contains(keyword))
                .toList();
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return the number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the tasks in display order.
     *
     * @return a read-only view of the tasks
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }
}
