package ax.stardust.runcal;

import java.util.HashMap;
import java.util.function.Function;

public class Model {
    public enum Property {
        CONVERT_PACE_TO_SPEED(Input.PACE, Input.NONE, Calculator::convertPaceToSpeed),
        CONVERT_SPEED_TO_PACE(Input.SPEED, Input.NONE, Calculator::convertSpeedToPace),
        CALCULATE_PACE(Input.DISTANCE, Input.TIME, Calculator::calculatePace),
        CALCULATE_TIME(Input.DISTANCE, Input.PACE, Calculator::calculateTime),
        CALCULATE_DISTANCE(Input.TIME, Input.PACE, Calculator::calculateDistance);

        private final Function<String, String> calculatorFunction;

        private final Input firstInput;
        private final Input secondInput;

        Property(Input firstInput, Input secondInput, Function<String, String> calculatorFunction) {
            this.firstInput = firstInput;
            this.secondInput = secondInput;
            this.calculatorFunction = calculatorFunction;
        }

        public boolean isPairedInput() {
            return !Input.NONE.equals(firstInput) && !Input.NONE.equals(secondInput);
        }

        public Function<String, String> getCalculatorFunction() {
            return calculatorFunction;
        }
    }

    public enum Input {
        PACE(':'),
        SPEED('.'),
        DISTANCE('.'),
        TIME(':'),
        NONE(' ');

        private final char separator;

        Input(char separator) {
            this.separator = separator;
        }

        public char getSeparator() {
            return separator;
        }
    }

    public enum ValueSelection {
        FIRST,SECOND
    }

    private final HashMap<Property, PairedValue> data = new HashMap();

    public boolean changed() {
        return data.entrySet().stream().anyMatch(entry -> entry.getValue().changed(entry.getKey()));
    }

    public void commitProperty(Property property) {
        data.getOrDefault(property, new PairedValue()).commit();
    }

    public boolean isPropertyChanged(Property property) {
        return data.getOrDefault(property, new PairedValue()).changed(property);
    }

    public String getPropertyValue(Property property) {
        return data.getOrDefault(property, new PairedValue()).getValue(property);
    }

    public void setPropertyValue(Property property, ValueSelection selection, String value) {
        PairedValue pairedValue = data.getOrDefault(property, new PairedValue());
        pairedValue.setValue(selection, value);
        data.put(property, pairedValue);
    }

    private static class PairedValue {
        private final Value firstValue = new Value();
        private final Value secondValue = new Value();

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

            String getValue() {
                return value;
            }

            void setValue(String value) {
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
