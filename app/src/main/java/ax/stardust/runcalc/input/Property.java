package ax.stardust.runcalc.input;

import java.util.function.Function;

import ax.stardust.runcalc.util.Calculator;

public enum Property {
    CONVERT_PACE_TO_SPEED(Calculator::convertPaceToSpeed),
    CONVERT_SPEED_TO_PACE(Calculator::convertSpeedToPace),
    CALCULATE_PACE(Calculator::calculatePace),
    CALCULATE_TIME(Calculator::calculateTime),
    CALCULATE_DISTANCE(Calculator::calculateDistance);

    private final Function<String, String> calculatorFunction;

    Property(Function<String, String> calculatorFunction) {
        this.calculatorFunction = calculatorFunction;
    }

    public Function<String, String> getCalculatorFunction() {
        return calculatorFunction;
    }
}