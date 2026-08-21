package janet.task;

import java.util.ArrayList;
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
        get(taskNumber);
        return tasks.remove(taskNumber - 1);
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
