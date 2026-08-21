package janet.logic;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import janet.exception.InvalidCommandException;
import janet.exception.InvalidTaskException;
import janet.task.Deadline;
import janet.task.Event;
import janet.task.Todo;

/**
 * Converts user-entered command text into executable commands.
 */
public class Parser {
    /**
     * Parses a complete user command.
     *
     * @param userInput command text entered by the user
     * @return a command ready for execution
     */
    public static Command parse(String userInput) {
        CommandName commandName = CommandName.from(userInput);
        return switch (commandName) {
        case LIST -> new ListCommand();
        case TODO -> new AddCommand(parseTodo(userInput));
        case EVENT -> new AddCommand(parseEvent(userInput));
        case DEADLINE -> new AddCommand(parseDeadline(userInput));
        case DELETE -> new DeleteCommand(parseTaskNumber(userInput, 7));
        case MARK -> new MarkCommand(parseTaskNumber(userInput, 5));
        case UNMARK -> new UnmarkCommand(parseTaskNumber(userInput, 7));
        case BYE -> new ExitCommand();
        case UNKNOWN -> throw new InvalidCommandException("OOPS!!! I don't recognize that command.");
        };
    }

    /**
     * Prevents construction of this utility class.
     */
    private Parser() {
    }

    /**
     * Parses a to-do command.
     *
     * @param userInput complete command text
     * @return the to-do task described by the command
     */
    private static Todo parseTodo(String userInput) {
        String description = userInput.length() > 5 ? userInput.substring(5).trim() : "";
        if (description.isEmpty()) {
            throw new InvalidCommandException("OOPS!!! A todo needs a description.");
        }
        return new Todo(description);
    }

    /**
     * Parses an event command.
     *
     * @param userInput complete command text
     * @return the event task described by the command
     */
    private static Event parseEvent(String userInput) {
        String eventCommand = userInput.length() > 6 ? userInput.substring(6).trim() : "";
        int fromIndex = eventCommand.indexOf(" /from ");
        int toIndex = eventCommand.indexOf(" /to ");
        if (fromIndex <= 0 || toIndex <= fromIndex) {
            throw new InvalidCommandException("Sorry, please use: event <task> /from <start> /to <end>.");
        }

        String description = eventCommand.substring(0, fromIndex).trim();
        String start = toIndex >= fromIndex + 7
                ? eventCommand.substring(fromIndex + 7, toIndex).trim()
                : "";
        String end = eventCommand.substring(toIndex + 5).trim();
        if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
            throw new InvalidCommandException("OOPS!!! An event needs a description, start, and end.");
        }
        return new Event(description, start, end);
    }

    /**
     * Parses a deadline command.
     *
     * @param userInput complete command text
     * @return the deadline task described by the command
     */
    private static Deadline parseDeadline(String userInput) {
        String deadlineCommand = userInput.length() > 9 ? userInput.substring(9).trim() : "";
        int byIndex = deadlineCommand.indexOf(" /by ");
        if (byIndex <= 0) {
            throw new InvalidCommandException("Sorry, please use: deadline <task> /by <date or time>.");
        }

        String description = deadlineCommand.substring(0, byIndex).trim();
        String deadlineText = deadlineCommand.substring(byIndex + 5).trim();
        if (description.isEmpty() || deadlineText.isEmpty()) {
            throw new InvalidCommandException("OOPS!!! A deadline needs a description and due time.");
        }
        try {
            return new Deadline(description, LocalDate.parse(deadlineText));
        } catch (DateTimeParseException exception) {
            throw new InvalidCommandException("Sorry, please provide a deadline date in yyyy-MM-dd format.");
        }
    }

    /**
     * Parses the one-based task number at the end of a task command.
     *
     * @param userInput complete command text
     * @param numberStart index where the task number begins
     * @return the parsed task number
     */
    private static int parseTaskNumber(String userInput, int numberStart) {
        if (userInput.length() <= numberStart) {
            throw new InvalidTaskException("Sorry, please provide a valid task number.");
        }

        try {
            return Integer.parseInt(userInput.substring(numberStart).trim());
        } catch (NumberFormatException exception) {
            throw new InvalidTaskException("Sorry, please provide a valid task number.");
        }
    }
}
