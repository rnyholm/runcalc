package ax.stardust.runcalc.input;

public enum Input {
    PACE(":"),
    SPEED("."),
    DISTANCE("."),
    TIME(":"),
    HEART_RATE(""),
    AGE(""),
    NONE("");

    private final String separator;

    Input(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }
}