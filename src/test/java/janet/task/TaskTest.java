package janet.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class TaskTest {
    @Test
    void todo_statusTransitions_preservesDescriptionAndType() {
        Todo todo = new Todo("read book");

        assertEquals("[T]", todo.getTypeIcon());
        assertEquals("read book", todo.getDescription());
        assertEquals("read book", todo.getRawDescription());
        assertEquals(" ", todo.getStatusIcon());
        assertFalse(todo.isDone());

        todo.markAsDone();
        assertEquals("X", todo.getStatusIcon());
        assertTrue(todo.isDone());

        todo.markAsUndone();
        assertEquals(" ", todo.getStatusIcon());
        assertFalse(todo.isDone());
    }

    @Test
    void deadline_getters_returnFormattedAndRawDetails() {
        Deadline deadline = new Deadline("return book", LocalDate.of(2019, 12, 2));

        assertEquals("[D]", deadline.getTypeIcon());
        assertEquals(LocalDate.of(2019, 12, 2), deadline.getDeadline());
        assertEquals("return book", deadline.getRawDescription());
        assertEquals("return book (by: Dec 02 2019)", deadline.getDescription());
    }

    @Test
    void event_getters_returnFormattedAndRawDetails() {
        Event event = new Event("project meeting", "Mon 2pm", "4pm");

        assertEquals("[E]", event.getTypeIcon());
        assertEquals("Mon 2pm", event.getStart());
        assertEquals("4pm", event.getEnd());
        assertEquals("project meeting", event.getRawDescription());
        assertEquals("project meeting (from: Mon 2pm to: 4pm)", event.getDescription());
    }
}
