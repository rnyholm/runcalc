package ax.stardust.runnerscalculator;

import java.util.HashMap;
import java.util.function.Function;

public class Model {
    enum Property {
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

    private HashMap<Property, Values> data = new HashMap();

    public boolean changed() {
        return data.values().stream().anyMatch(values -> values.changed());
    }

    public void commit() {
        data.values().forEach(values -> values.commit());
    }

    public void commitProperty(Property property) {
        data.getOrDefault(property, new Values()).commit();
    }

    public boolean isPropertyChanged(Property property) {
        return data.getOrDefault(property, new Values()).changed();
    }

    public String getPropertyValue(Property property) {
        return data.getOrDefault(property, new Values()).getVal();
    }

    public void setPropertyValue(Property property, String value) {
        Values values = data.getOrDefault(property, new Values());
        values.setVal(value);
        data.put(property, values);
    }

    private static class Values {
        private String existingVal = "";
        private String val = "";

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public void commit() {
            existingVal = val;
        }

        private boolean changed() {
            if (existingVal == null) {
                return val == null;
            }
            return existingVal.equals(val);
        }
    }
}
