/**
 * The commands Janet understands at the start of user input.
 */
public enum CommandName {
    LIST,
    TODO,
    EVENT,
    DEADLINE,
    DELETE,
    MARK,
    UNMARK,
    BYE,
    UNKNOWN;

    /**
     * Parses the command name from a complete user input.
     *
     * @param command the complete user input
     * @return the matching command name, or {@link #UNKNOWN}
     */
    public static CommandName from(String command) {
        if (command.equals("list")) {
            return LIST;
        }
        if (command.equals("bye")) {
            return BYE;
        }

        int firstSpace = command.indexOf(' ');
        String name = firstSpace < 0 ? command : command.substring(0, firstSpace);
        try {
            return CommandName.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException exception) {
            return UNKNOWN;
        }
    }
}
