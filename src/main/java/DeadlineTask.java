public class DeadlineTask extends Task {
    private String by;

    public DeadlineTask(String description, String by) {
        super(description, 'D');
        this.by = by;
    }

    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + this.by + ")";
    }
}
