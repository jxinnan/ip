/**
 * Base exception for errors specific to Janet's command handling.
 */
public class JanetException extends RuntimeException {
    /**
     * Creates a Janet-specific exception with a user-facing message.
     *
     * @param message the message to display to the user
     */
    public JanetException(String message) {
        super(message);
    }
}

/** Indicates that Janet has reached its task capacity. */
class TaskLimitException extends JanetException {
    TaskLimitException(String message) {
        super(message);
    }
}

/** Indicates that a structured command is malformed. */
class InvalidCommandException extends JanetException {
    InvalidCommandException(String message) {
        super(message);
    }
}

/** Indicates that a task number is invalid or does not exist. */
class InvalidTaskException extends JanetException {
    InvalidTaskException(String message) {
        super(message);
    }
}
