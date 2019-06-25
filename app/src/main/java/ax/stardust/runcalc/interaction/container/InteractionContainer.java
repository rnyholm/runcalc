package ax.stardust.runcalc.interaction.container;

import android.text.Editable;

import ax.stardust.runcalc.component.widget.KeyboardlessEditText;
import ax.stardust.runcalc.function.Property;

public interface InteractionContainer extends Comparable {
    /**
     * To get the {@link Property} of this interaction container.
     *
     * @return Property of this interaction container.
     */
    Property getProperty();

    /**
     * To make calculations, if possible, depending on components and data within this
     * interaction container.
     */
    void calculateIfPossible();

    /**
     * Set necessary listeners to this interaction container, depending on
     * what components it contains.
     */
    void setListeners();

    /**
     * Sets the default results text(s) to this interaction container.
     */
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
