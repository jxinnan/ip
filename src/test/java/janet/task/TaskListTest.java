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
    void delete_multipleExistingTasks_removesAtomicallyAndReturnsRequestedOrder() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));
        tasks.add(new Todo("third"));
        tasks.add(new Todo("fourth"));

        List<Task> deletedTasks = tasks.delete(List.of(4, 2));

        assertEquals(List.of("fourth", "second"), deletedTasks.stream()
                .map(Task::getDescription)
                .toList());
        assertEquals(List.of("first", "third"), tasks.getTasks().stream()
                .map(Task::getDescription)
                .toList());
    }

    @Test
    void delete_duplicateOrInvalidTaskNumbers_throwsExceptionWithoutRemovingTasks() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("first"));
        tasks.add(new Todo("second"));
        tasks.add(new Todo("third"));

        assertThrows(InvalidTaskException.class, () -> tasks.delete(List.of()));
        assertThrows(InvalidTaskException.class, () -> tasks.delete(List.of(1, 1)));
        assertThrows(InvalidTaskException.class, () -> tasks.delete(List.of(2, 4)));
        assertEquals(List.of("first", "second", "third"), tasks.getTasks().stream()
                .map(Task::getDescription)
                .toList());
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
