package ax.stardust.runcalc.function;

import java.util.function.Function;

public enum Property {
    CONVERT_PACE_TO_SPEED(Calculator::convertPaceToSpeed),
    CONVERT_SPEED_TO_PACE(Calculator::convertSpeedToPace),
    CALCULATE_PACE(Calculator::calculatePace),
    CALCULATE_TIME(Calculator::calculateTime),
    CALCULATE_DISTANCE(Calculator::calculateDistance),
    CALCULATE_VO2MAX_ESTIMATE(Calculator::calculateVO2MaxEstimate),
    CALCULATE_HEART_RATE_ZONES(Calculator::calculateHeartRateZones),
    CALCULATE_FINISH_TIME_PREDICTIONS(Calculator::calculateFinishTimePredictions);

    private final Function<String, String> calculatorFunction;

    Property(Function<String, String> calculatorFunction) {
        this.calculatorFunction = calculatorFunction;
    }

    public Function<String, String> getCalculatorFunction() {
        return calculatorFunction;
    }
}