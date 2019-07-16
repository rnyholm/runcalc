package ax.stardust.runcalc.function;

import com.google.gson.annotations.SerializedName;

/**
 * Representation of the different distances used while making finish time predictions.
 * NOTE! All lengths are in meters in decimal format!
 */
public enum PredictionDistance {
    @SerializedName("D_100_M")
    D_100_M(100.00),
    @SerializedName("D_200_M")
    D_200_M(200.00),
    @SerializedName("D_400_M")
    D_400_M(400.00),
    @SerializedName("D_800_M")
    D_800_M(800.00),
    @SerializedName("D_1500_M")
    D_1500_M(1500.00),
    @SerializedName("D_1_MI")
    D_1_MI(1609.34),
    @SerializedName("D_2_MI")
    D_2_MI(3218.68),
    @SerializedName("D_5_K")
    D_5_K(5000.00),
    @SerializedName("D_5_MI")
    D_5_MI(8046.72),
    @SerializedName("D_10_K")
    D_10_K(10000.00),
    @SerializedName("D_10_MI")
    D_10_MI(16093.44),
    @SerializedName("D_HALF_MARATHON")
    D_HALF_MARATHON(21097.50),
    @SerializedName("D_MARATHON")
    D_MARATHON(42195.00),
    @SerializedName("D_50_K")
    D_50_K(50000.00),
    @SerializedName("D_50_MI")
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
