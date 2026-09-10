package janet.logic;

import java.util.List;

import janet.storage.Storage;
import janet.task.Task;
import janet.task.TaskList;
import janet.ui.Ui;

/**
 * Represents an operation entered by the user.
 */
public abstract class Command {
    /**
     * Executes this command using Janet's collaborating components.
     *
     * @param tasks Janet's task list
     * @param ui Janet's user interface
     * @param storage Janet's task storage
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage);

    /**
     * Returns whether executing this command ends Janet.
     *
     * @return whether Janet should exit
     */
    public boolean isExit() {
        return false;
    }
}

/** Adds a new task and persists the changed list. */
class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds one task.
     *
     * @param task task to add
     */
    AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }
}

/** Displays every stored task. */
class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks.getTasks());
    }
}

/** Removes one or more tasks and persists the changed list. */
class DeleteCommand extends Command {
    private final List<Integer> taskNumbers;

    /**
     * Creates a command that deletes one or more tasks.
     *
     * @param taskNumbers one-based numbers of the tasks to delete
     */
    DeleteCommand(List<Integer> taskNumbers) {
        this.taskNumbers = List.copyOf(taskNumbers);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> deletedTasks = tasks.delete(taskNumbers);
        storage.save(tasks);
        if (deletedTasks.size() == 1) {
            ui.showTaskDeleted(deletedTasks.get(0), tasks.size());
        } else {
            ui.showTasksDeleted(deletedTasks, tasks.size());
        }
    }
}

/** Marks a task as complete and persists the changed list. */
class MarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command that marks one task as complete.
     *
     * @param taskNumber one-based number of the task to mark
     */
    MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task task = tasks.get(taskNumber);
        task.markAsDone();
        storage.save(tasks);
        ui.showTaskMarked(task);
    }
}

/** Marks a task as incomplete and persists the changed list. */
class UnmarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command that marks one task as incomplete.
     *
     * @param taskNumber one-based number of the task to unmark
     */
    UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task task = tasks.get(taskNumber);
        task.markAsUndone();
        storage.save(tasks);
        ui.showTaskUnmarked(task);
    }
}

/** Displays tasks whose descriptions contain a keyword. */
class FindCommand extends Command {
    private final String keyword;

    FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showMatchingTasks(tasks.find(keyword));
    }
}

/** Ends Janet after showing the goodbye message. */
class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
