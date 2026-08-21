package janet.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import janet.task.Deadline;
import janet.task.Task;
import janet.task.TaskList;
import janet.task.Todo;

class StorageTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void saveAndLoad_multipleTaskTypes_preservesTaskDetailsAndStatus() {
        Storage storage = new Storage(temporaryDirectory.resolve("data/tasks.txt").toString());
        TaskList originalTasks = new TaskList();
        Todo completedTodo = new Todo("read book");
        completedTodo.markAsDone();
        originalTasks.add(completedTodo);
        originalTasks.add(new Deadline("return book", LocalDate.of(2019, 12, 2)));

        storage.save(originalTasks);
        TaskList loadedTasks = new TaskList(storage.load());

        assertEquals(2, loadedTasks.size());
        Task loadedTodo = loadedTasks.get(1);
        assertEquals("read book", loadedTodo.getRawDescription());
        assertTrue(loadedTodo.isDone());
        assertEquals("return book (by: Dec 02 2019)", loadedTasks.get(2).getDescription());
    }
}
