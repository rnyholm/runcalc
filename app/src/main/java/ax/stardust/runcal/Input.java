package ax.stardust.runcal;

public enum Input {
    PACE(":"),
    SPEED("."),
    DISTANCE("."),
    TIME(":"),
    NONE("");

    private final String separator;

    Input(String separator) {
        this.separator = separator;
    }

    public String getSeparator() {
        return separator;
    }
}