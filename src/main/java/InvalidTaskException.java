/** Indicates that a task number is invalid or does not exist. */
class InvalidTaskException extends JanetException {
    InvalidTaskException(String message) {
        super(message);
    }
}
