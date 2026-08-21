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

/** Removes a task and persists the changed list. */
class DeleteCommand extends Command {
    private final int taskNumber;

    DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        Task deletedTask = tasks.delete(taskNumber);
        storage.save(tasks);
        ui.showTaskDeleted(deletedTask, tasks.size());
    }
}

/** Marks a task as complete and persists the changed list. */
class MarkCommand extends Command {
    private final int taskNumber;

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
