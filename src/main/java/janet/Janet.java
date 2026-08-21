package janet;

import janet.exception.JanetException;
import janet.logic.Command;
import janet.logic.Parser;
import janet.storage.Storage;
import janet.task.TaskList;
import janet.ui.Ui;

/**
 * Runs Janet, a command-line task manager.
 */
public class Janet {
    /** Location of Janet's data file relative to the working directory. */
    private static final String DATA_FILE_PATH = "data/janet.txt";

    /** Handles persisted task data. */
    private final Storage storage;

    /** Holds the tasks managed during this run. */
    private final TaskList tasks;

    /** Handles console input and output. */
    private final Ui ui;

    /**
     * Creates Janet and loads tasks saved during a previous run.
     */
    public Janet() {
        ui = new Ui();
        storage = new Storage(DATA_FILE_PATH);
        tasks = new TaskList(storage.load());
    }

    /**
     * Starts Janet's command-processing loop.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            try {
                Command command = Parser.parse(ui.readCommand());
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (JanetException exception) {
                ui.showError(exception.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    /**
     * Starts Janet from the command line.
     *
     * @param args command-line arguments, which Janet does not use
     */
    public static void main(String[] args) {
        new Janet().run();
    }
}
