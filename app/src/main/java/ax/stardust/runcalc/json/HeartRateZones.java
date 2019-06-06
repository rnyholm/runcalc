package ax.stardust.runcalc.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class HeartRateZones {
    public static final String ZONE_5 = "z5";
    public static final String ZONE_4 = "z4";
    public static final String ZONE_3 = "z3";
    public static final String ZONE_2 = "z2";
    public static final String ZONE_1 = "z1";

    private final List<HeartRateZone> heartRateZones = new ArrayList<>();

    public HeartRateZones() {
    }

    public void addZone(HeartRateZone heartRateZone) {
        heartRateZones.add(heartRateZone);
    }

    public HeartRateZone getZone(String zone) {
        if (!Arrays.asList(ZONE_5, ZONE_4, ZONE_3, ZONE_2, ZONE_1).contains(zone)) {
            throw new IllegalArgumentException();
        }

        Optional<HeartRateZone> optionalTrainingHeartRateZone = heartRateZones.stream()
                .filter(thrz -> zone.equals(thrz.getZone()))
                .findFirst();

        if (!optionalTrainingHeartRateZone.isPresent()) {
            throw new IllegalStateException();
        }

        return optionalTrainingHeartRateZone.get();
    }

    @Override
    public String toString() {
        return "(" + heartRateZones + ")";
    }

    public static class HeartRateZone {
        private final String zone;

        private final int hrMin;
        private final int hrMax;
        private final int percentageMin;
        private final int percentageMax;

        private HeartRateZone(String zone, int hrMin, int hrMax, int percentageMin, int percentageMax) {
            this.zone = zone;
            this.hrMin = hrMin;
            this.hrMax = hrMax;
            this.percentageMin = percentageMin;
            this.percentageMax = percentageMax;
        }

        public static HeartRateZone create(String name, int hrMin, int hrMax, int percentageMin, int percentageMax) {
            return new HeartRateZone(name, hrMin, hrMax, percentageMin, percentageMax);
        }

        public String getZone() {
            return zone;
        }

        public int getHrMin() {
            return hrMin;
        }

        public int getHrMax() {
            return hrMax;
        }

        public int getPercentageMin() {
            return percentageMin;
        }

        public int getPercentageMax() {
            return percentageMax;
        }

        @Override
        public String toString() {
            return zone + " - " + hrMin + " - " + hrMax + " - " + percentageMin  + " - " + percentageMax;
        }
    }
}
