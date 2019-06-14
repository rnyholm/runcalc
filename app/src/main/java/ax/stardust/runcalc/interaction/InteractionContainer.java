package ax.stardust.runcalc.interaction;

import android.text.Editable;

import ax.stardust.runcalc.component.KeyboardlessEditText;
import ax.stardust.runcalc.input.Property;

public interface InteractionContainer {
    void calculateIfPossible();
    Property getProperty();
    void setListeners();
    void setDefaultResults();

    default String getTextOfInput(KeyboardlessEditText input) {
        String text = "";

        if (input != null) {
            Editable editable = input.getText();
            text = editable != null ? editable.toString() : "";
        }

        return text;
    }

    default boolean hasValidCalculationInput(KeyboardlessEditText input) {
        if (input != null) {
            String text = getTextOfInput(input);
            if (!text.isEmpty()) {
                try {
                    input.getValidatorFunction().apply(text);
                    return true;
                } catch (Exception e) {
                    // catch it and let it fall through
                }
            }
        }
        return false;
    }
}
