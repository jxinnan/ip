/** Indicates that a structured command is malformed. */
class InvalidCommandException extends JanetException {
    InvalidCommandException(String message) {
        super(message);
    }
}
