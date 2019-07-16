package ax.stardust.runcalc.function;

/**
 * Representation of the different distances used while making finish time predictions.
 * NOTE! All lengths are in meters in decimal format!
 */
public enum PredictionDistance {
    D_100_M(100.00),
    D_200_M(200.00),
    D_400_M(400.00),
    D_800_M(800.00),
    D_1500_M(1500.00),
    D_1_MI(1609.34),
    D_2_MI(3218.68),
    D_5_K(5000.00),
    D_5_MI(8046.72),
    D_10_K(10000.00),
    D_10_MI(16093.44),
    D_HALF_MARATHON(21097.50),
    D_MARATHON(42195.00),
    D_50_K(50000.00),
    D_50_MI(80467.20);

    private final double distance;

    PredictionDistance(double distance) {
        this.distance = distance;
    }

    private double inMeters() {
        return distance;
    }

    public double inKilometers() {
        return inMeters() * 0.001;
    }
}
