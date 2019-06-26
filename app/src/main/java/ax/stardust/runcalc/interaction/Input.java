package ax.stardust.runcalc.interaction;

public enum Input {
    AGE(""),
    DISTANCE_KM("."),
    DISTANCE_M(""),
    HEART_RATE(""),
    NONE(""),
    PACE(":"),
    SPEED("."),
    TIME(":");

    private final String separator;

    Input(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }
}