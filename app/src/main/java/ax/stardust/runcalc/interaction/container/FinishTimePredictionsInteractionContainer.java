package ax.stardust.runcalc.interaction.container;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ax.stardust.runcalc.component.keyboard.KeyboardHandler;
import ax.stardust.runcalc.component.keyboard.RunnersKeyboard;
import ax.stardust.runcalc.component.watcher.ReferencedTextWatcher;
import ax.stardust.runcalc.component.widget.KeyboardlessEditText;
import ax.stardust.runcalc.function.Property;

public class FinishTimePredictionsInteractionContainer implements InteractionContainer {
    private Context context;
    private Property property;
    private RunnersKeyboard keyboard;
    private KeyboardlessEditText finishTimeInput;
    private RadioGroup distanceInputGroup;
    private RadioButton distance5kmInput;
    private RadioButton distance10kmInput;

    private FinishTimePredictionsInteractionContainer() {
    }

    @Override
    public Property getProperty() {
        return property;
    }

    @Override
    public void calculateIfPossible() {
        // TODO: Implement this shit...
    }

    @Override
    public void setListeners() {
        finishTimeInput.addTextChangedListener(new ReferencedTextWatcher(this, finishTimeInput, keyboard));
        finishTimeInput.setOnFocusChangeListener(new KeyboardHandler(keyboard));
        finishTimeInput.setOnTouchListener(new KeyboardHandler(keyboard));

        // TODO: add listeners for the radio group and it's buttons
    }

    @Override
    public void setDefaultResults() {
        // TODO: implement this shit...
    }

    @Override
    public int compareTo(@NonNull Object that) {
        return this.property.compareTo(((InteractionContainer) that).getProperty());
    }
}
