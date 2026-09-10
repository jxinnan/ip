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
     * Prevents construction of this utility class.
     */
    private Parser() {
    }

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
            case DELETE -> new DeleteCommand(parseTaskNumber(userInput));
            case MARK -> new MarkCommand(parseTaskNumber(userInput));
            case UNMARK -> new UnmarkCommand(parseTaskNumber(userInput));
            case FIND -> new FindCommand(parseKeyword(userInput));
            case BYE -> new ExitCommand();
            case UNKNOWN -> throw new InvalidCommandException("OOPS!!! I don't recognize that command.");
        };
    }

    /**
     * Parses a to-do command.
     *
     * @param userInput complete command text
     * @return the to-do task described by the command
     */
    private static Todo parseTodo(String userInput) {
        String description = parseArgument(userInput);
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
        String eventCommand = parseArgument(userInput);
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
        String deadlineCommand = parseArgument(userInput);
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
     * @return the parsed task number
     */
    private static int parseTaskNumber(String userInput) {
        String taskNumber = parseArgument(userInput);
        if (taskNumber.isEmpty()) {
            throw new InvalidTaskException("Sorry, please provide a valid task number.");
        }

        try {
            return Integer.parseInt(taskNumber);
        } catch (NumberFormatException exception) {
            throw new InvalidTaskException("Sorry, please provide a valid task number.");
        }
    }

    /**
     * Parses the keyword supplied to a find command.
     *
     * @param userInput complete command text
     * @return the non-empty keyword to search for
     */
    private static String parseKeyword(String userInput) {
        String keyword = parseArgument(userInput);
        if (keyword.isEmpty()) {
            throw new InvalidCommandException("OOPS!!! A find command needs a keyword.");
        }
        return keyword;
    }

    /**
     * Returns the trimmed text after a command name.
     *
     * @param userInput complete command text
     * @return the command argument, or an empty string when none was supplied
     */
    private static String parseArgument(String userInput) {
        int firstSpace = userInput.indexOf(' ');
        return firstSpace < 0 ? "" : userInput.substring(firstSpace + 1).trim();
    }
}
