package janet.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import janet.exception.InvalidTaskException;

class TaskListTest {
    @Test
    void delete_existingTask_removesAndReturnsSelectedTask() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));

        Task deletedTask = tasks.delete(1);

        assertEquals("first", deletedTask.getDescription());
        assertEquals(1, tasks.size());
        assertEquals("second", tasks.get(1).getDescription());
    }

    @Test
    void get_invalidTaskNumber_throwsException() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("only task"));

        assertThrows(InvalidTaskException.class, () -> tasks.get(0));
        assertThrows(InvalidTaskException.class, () -> tasks.get(2));
    }
}
