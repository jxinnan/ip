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