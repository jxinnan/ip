package janet.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import janet.exception.StorageException;
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

    @Test
    void load_missingFile_returnsEmptyListWithoutWarning() {
        Storage storage = new Storage(temporaryDirectory.resolve("missing/tasks.txt").toString());

        List<Task> loadedTasks = storage.load();

        assertTrue(loadedTasks.isEmpty());
        assertTrue(storage.getLoadWarnings().isEmpty());
    }

    @Test
    void load_malformedLines_loadsValidTasksAndBlocksSaving() throws IOException {
        Path dataFile = temporaryDirectory.resolve("data/tasks.txt");
        Files.createDirectories(dataFile.getParent());
        String originalData = "T\t0\tread book\ninvalid row\nD\t1\treturn book\t2019-12-02\n";
        Files.writeString(dataFile, originalData);
        Storage storage = new Storage(dataFile.toString());

        List<Task> loadedTasks = storage.load();

        assertEquals(2, loadedTasks.size());
        assertEquals(1, storage.getLoadWarnings().size());
        assertTrue(storage.getLoadWarnings().get(0).contains("line 2"));
        assertThrows(StorageException.class, () -> storage.save(new TaskList(loadedTasks)));
        assertEquals(originalData, Files.readString(dataFile));
    }

    @Test
    void load_dataPathIsDirectory_warnsAndBlocksSaving() throws IOException {
        Path directoryPath = temporaryDirectory.resolve("tasks-directory");
        Files.createDirectory(directoryPath);
        Storage storage = new Storage(directoryPath.toString());

        List<Task> loadedTasks = storage.load();

        assertTrue(loadedTasks.isEmpty());
        assertEquals(1, storage.getLoadWarnings().size());
        assertTrue(storage.getLoadWarnings().get(0).contains("couldn't read"));
        assertThrows(StorageException.class, () -> storage.save(new TaskList()));
    }

    @Test
    void save_parentPathIsFile_throwsStorageException() throws IOException {
        Path blockingParent = temporaryDirectory.resolve("not-a-directory");
        Files.writeString(blockingParent, "file blocks directory creation");
        Storage storage = new Storage(blockingParent.resolve("tasks.txt").toString());
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        StorageException exception = assertThrows(StorageException.class, () -> storage.save(tasks));

        assertTrue(exception.getMessage().contains("couldn't save"));
    }
}
