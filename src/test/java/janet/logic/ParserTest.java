package janet.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import janet.exception.InvalidCommandException;
import janet.exception.InvalidTaskException;

class ParserTest {
    @Test
    void parse_allValidCommandTypes_returnsExpectedCommandClasses() {
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
        assertInstanceOf(AddCommand.class, Parser.parse("todo read book"));
        assertInstanceOf(AddCommand.class, Parser.parse("deadline return book /by 2019-12-02"));
        assertInstanceOf(AddCommand.class, Parser.parse("event meeting /from 2pm /to 4pm"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1 3"));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1"));
        assertInstanceOf(FindCommand.class, Parser.parse("find book"));
        assertInstanceOf(ExitCommand.class, Parser.parse("bye"));
    }

    @Test
    void parse_surroundingAndRepeatedWhitespace_parsesCommand() {
        assertInstanceOf(ListCommand.class, Parser.parse("  list  "));
        assertInstanceOf(AddCommand.class, Parser.parse("todo    read book"));
        assertInstanceOf(AddCommand.class, Parser.parse("todo\tread book"));
    }

    @Test
    void parse_blankUnknownOrUnexpectedArgument_throwsSpecificException() {
        InvalidCommandException blankException = assertThrows(
                InvalidCommandException.class, () -> Parser.parse("   "));
        InvalidCommandException unknownException = assertThrows(
                InvalidCommandException.class, () -> Parser.parse("dance"));
        InvalidCommandException listException = assertThrows(
                InvalidCommandException.class, () -> Parser.parse("list extra"));
        InvalidCommandException byeException = assertThrows(
                InvalidCommandException.class, () -> Parser.parse("bye now"));

        assertEquals("Please enter a command.", blankException.getMessage());
        assertEquals("OOPS!!! I don't recognize that command.", unknownException.getMessage());
        assertEquals("Sorry, list does not take any arguments.", listException.getMessage());
        assertEquals("Sorry, bye does not take any arguments.", byeException.getMessage());
    }

    @Test
    void parse_taskWithoutRequiredDetails_throwsInvalidCommandException() {
        assertThrows(InvalidCommandException.class, () -> Parser.parse("todo"));
        assertThrows(InvalidCommandException.class, () -> Parser.parse("deadline return book"));
        assertThrows(InvalidCommandException.class, () -> Parser.parse("deadline return book /by"));
        assertThrows(InvalidCommandException.class, () -> Parser.parse("event meeting /from 2pm"));
        assertThrows(InvalidCommandException.class, () -> Parser.parse("event meeting /from /to 4pm"));
        assertThrows(InvalidCommandException.class, () -> Parser.parse("find"));
    }

    @Test
    void parse_invalidDeadlineDates_throwsInvalidCommandException() {
        assertThrows(InvalidCommandException.class, () ->
                Parser.parse("deadline return book /by 02-12-2019"));
        assertThrows(InvalidCommandException.class, () ->
                Parser.parse("deadline return book /by 2019-02-29"));
    }

    @Test
    void parse_invalidTaskNumbers_throwsInvalidTaskException() {
        assertThrows(InvalidTaskException.class, () -> Parser.parse("mark"));
        assertThrows(InvalidTaskException.class, () -> Parser.parse("mark one"));
        assertThrows(InvalidTaskException.class, () -> Parser.parse("unmark 1.5"));
        assertThrows(InvalidTaskException.class, () -> Parser.parse("delete"));
        assertThrows(InvalidTaskException.class, () -> Parser.parse("delete 1 two"));
    }

    @Test
    void parse_taskTextContainingControlCharacter_throwsInvalidCommandException() {
        assertThrows(InvalidCommandException.class, () -> Parser.parse("todo read\tbook"));
        assertThrows(InvalidCommandException.class, () ->
                Parser.parse("event meeting /from 2pm\tsharp /to 4pm"));
        assertThrows(InvalidCommandException.class, () ->
                Parser.parse("deadline return\tbook /by 2019-12-02"));
    }
}
