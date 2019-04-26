package ax.stardust.runcal;

import java.util.HashMap;
import java.util.function.Function;

public class Model {
    enum Property {
        CONVERT_PACE_TO_SPEED(false, Calculator::convertPaceToSpeed),
        CONVERT_SPEED_TO_PACE(false, Calculator::convertSpeedToPace),
        CALCULATE_PACE(true, Calculator::calculatePace),
        CALCULATE_TIME(true, Calculator::calculateTime),
        CALCULATE_DISTANCE(true, Calculator::calculateDistance);

        private final boolean pairedInput;
        private final Function<String, String> calculatorFunction;

        Property(boolean pairedInput, Function<String, String> calculatorFunction) {
            this.pairedInput = pairedInput;
            this.calculatorFunction = calculatorFunction;
        }

        public boolean isPairedInput() {
            return pairedInput;
        }

        public Function<String, String> getCalculatorFunction() {
            return calculatorFunction;
        }
    }

    enum ValueSelection {
        FIRST,SECOND
    }

    private HashMap<Property, PairedValue> data = new HashMap();

    boolean changed() {
        return data.entrySet().stream().anyMatch(entry -> entry.getValue().changed(entry.getKey()));
    }

    void commitProperty(Property property) {
        data.getOrDefault(property, new PairedValue()).commit();
    }

    boolean isPropertyChanged(Property property) {
        return data.getOrDefault(property, new PairedValue()).changed(property);
    }

    String getPropertyValue(Property property) {
        return data.getOrDefault(property, new PairedValue()).getValue(property);
    }

    void setPropertyValue(Property property, ValueSelection selection, String value) {
        PairedValue pairedValue = data.getOrDefault(property, new PairedValue());
        pairedValue.setValue(selection, value);
        data.put(property, pairedValue);
    }

    private static class PairedValue {
        private Value firstValue = new Value();
        private Value secondValue = new Value();

        String getValue(Property property) {
            if (property.isPairedInput()) {
                return firstValue.getValue() + "|" + secondValue.getValue();
            }
            return firstValue.getValue();
        }

        void setValue(ValueSelection selection, String value) {
            if (ValueSelection.FIRST.equals(selection)) {
                firstValue.setValue(value);
            } else {
                secondValue.setValue(value);
            }
        }

        void commit() {
            firstValue.commit();
            secondValue.commit();
        }

        boolean changed(Property property) {
            if (property.isPairedInput()) {
                if (!firstValue.isCommitted() && !secondValue.isCommitted()) {
                    return firstValue.changed() && secondValue.changed();
                }
                return firstValue.changed() || secondValue.changed();
            }
            return firstValue.changed();
        }

        private static class Value {
            private String existingValue = "";
            private String value = "";

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            void commit() {
                existingValue = value;
            }

            boolean isCommitted() {
                return existingValue != null && !existingValue.isEmpty();
            }

            boolean changed() {
                if (existingValue == null) {
                    return value == null;
                }

                return !value.isEmpty() && !existingValue.equals(value);
            }
        }
    }
}
