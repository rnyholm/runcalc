package ax.stardust.runcalc.pojo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ax.stardust.runcalc.function.PredictionDistance;

public class FinishTimePredictions {
    @SerializedName("finishTimePredictions")
    private final List<FinishTimePrediction> finishTimePredictions = new ArrayList<>();

    public void addPrediction(FinishTimePrediction finishTimePrediction) {
        finishTimePredictions.add(finishTimePrediction);
    }

    public FinishTimePrediction getPrediction(PredictionDistance distance) throws IllegalStateException {
        Optional<FinishTimePrediction> optionalFinishTimePrediction = finishTimePredictions.stream()
                .filter(finishTimePrediction -> distance.equals(finishTimePrediction.getDistance()))
                .findFirst();

        if (!optionalFinishTimePrediction.isPresent()) {
            throw new IllegalStateException();
        }

        return optionalFinishTimePrediction.get();
    }

    @NonNull
    @Override
    public String toString() {
        return "(" + finishTimePredictions + ")";
    }

    public static class FinishTimePrediction {
        private final PredictionDistance distance;

        private final String time;
        private final String pace;

        private FinishTimePrediction(PredictionDistance distance, String time, String pace) {
            this.distance = distance;
            this.time = time;
            this.pace = pace;
        }

        public static FinishTimePrediction create(PredictionDistance distance, String time, String pace) {
            return new FinishTimePrediction(distance, time, pace);
        }

        public PredictionDistance getDistance() {
            return distance;
        }

        public String getTime() {
            return time;
        }

        public String getPace() {
            return pace;
        }

        @NonNull
        @Override
        public String toString() {
            return distance + " - " + time + " - " + pace;
        }
    }
}
