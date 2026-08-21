/** Indicates that Janet has reached its task capacity. */
class TaskLimitException extends JanetException {
    TaskLimitException(String message) {
        super(message);
    }
}
