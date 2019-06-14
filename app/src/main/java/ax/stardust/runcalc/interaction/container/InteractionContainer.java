package ax.stardust.runcalc.interaction.container;

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
}
