/**
 * Represents a task that must be completed before a specified date or time.
 */
public class Deadline extends Task {
    /** The date or time by which this task should be completed. */
    private String deadline;

    /**
     * Creates a new incomplete deadline task.
     *
     * @param description the text describing the task
     * @param deadline the date or time by which the task is due
     */
    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * Returns the deadline in the form it was supplied by the user.
     *
     * @return the unformatted deadline
     */
    public String getDeadline() {
        return deadline;
    }

    /**
     * Returns the icon identifying this task as a deadline.
     *
     * @return the deadline type icon
     */
    @Override
    public String getTypeIcon() {
        return "[D]";
    }

    /**
     * Returns the task description together with its deadline.
     *
     * @return the description and deadline
     */
    @Override
    public String getDescription() {
        return description + " (by: " + deadline + ")";
    }
}
