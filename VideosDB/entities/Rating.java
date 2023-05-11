package entities;

public final class Rating {
    private final String userOrTitle;
    private final double value;

    public Rating(final String userOrTitle, final double value) {
        this.userOrTitle = userOrTitle;
        this.value = value;
    }

    public String getUserOrTitle() {
        return userOrTitle;
    }

    public double getValue() {
        return value;
    }
}
