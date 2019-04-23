package ax.stardust.runnerscalculator;

import java.util.HashMap;

public class Model {
    enum Properties {
        PACE_TO_SPEED,
        SPEED_TO_PACE
    }

    private HashMap<Properties, Values> data = new HashMap();

    public boolean changed() {
        return data.values().stream().anyMatch(values -> values.changed());
    }

    public void commit() {
        data.values().forEach(values -> values.commit());
    }

    public String getPropertyValue(Properties property) {
        return data.getOrDefault(property, new Values()).getExistingVal();
    }

    public void setPropertyValue(Properties property, String value) {
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
