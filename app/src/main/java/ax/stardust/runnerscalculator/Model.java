package ax.stardust.runnerscalculator;

import java.util.HashMap;
import java.util.function.Consumer;

public class Model {
    enum Property {
        PACE_TO_SPEED(Calculator::convertPaceToSpeed),
        SPEED_TO_PACE(Calculator::convertSpeedToPace);

        private final Consumer calculatorFunction;

        Property(Consumer<String> calculatorFunction) {
            this.calculatorFunction = calculatorFunction;
        }

        public Consumer getCalculatorFunction() {
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

    public boolean isPropertyChanged(Property property) {
        return data.getOrDefault(property, new Values()).changed();
    }

    public String getExistingPropertyValue(Property property) {
        return data.getOrDefault(property, new Values()).getExistingVal();
    }

    public String getNewPropertyValue(Property property) {
        return data.getOrDefault(property, new Values()).getNewVal();
    }

    public void setPropertyValue(Property property, String value) {
        Values values = data.getOrDefault(property, new Values());
        values.setNewVal(value);
        data.put(property, values);
    }

    private static class Values {
        private String existingVal = "";
        private String newVal = "";

        public String getExistingVal() {
            return existingVal;
        }

        public String getNewVal() {
            return newVal;
        }

        public void setNewVal(String newVal) {
            this.newVal = newVal;
        }

        public void commit() {
            existingVal = newVal;
        }

        private boolean changed() {
            if (existingVal == null) {
                return newVal == null;
            }
            return existingVal.equals(newVal);
        }
    }
}
