package janet.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

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

    @Test
    void find_matchingKeyword_returnsMatchingTasksInOriginalOrder() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("borrow book"));
        tasks.add(new Todo("call friend"));

        List<Task> matchingTasks = tasks.find("book");

        assertEquals(2, matchingTasks.size());
        assertEquals("read book", matchingTasks.get(0).getDescription());
        assertEquals("borrow book", matchingTasks.get(1).getDescription());
    }
}
