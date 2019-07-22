package ax.stardust.runcalc.component.watcher;

import android.text.Editable;
import android.text.TextWatcher;

import ax.stardust.runcalc.R;
import ax.stardust.runcalc.component.keyboard.RunnersKeyboard;
import ax.stardust.runcalc.component.widget.KeyboardlessEditText;
import ax.stardust.runcalc.egg.EasterEgg;
import ax.stardust.runcalc.interaction.Input;
import ax.stardust.runcalc.interaction.container.InteractionContainer;

public class ReferencedTextWatcher implements TextWatcher {
    private final InteractionContainer interactionContainer;
    private final KeyboardlessEditText input;
    private final RunnersKeyboard runnersKeyboard;

    public ReferencedTextWatcher(InteractionContainer interactionContainer, KeyboardlessEditText input, RunnersKeyboard runnersKeyboard) {
        this.interactionContainer = interactionContainer;
        this.input = input;
        this.runnersKeyboard = runnersKeyboard;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Do nothing...
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String inputText = charSequence.toString();
        if (!inputText.isEmpty()) {
            try {
                input.getValidatorFunction().apply(inputText);
                interactionContainer.calculateIfPossible();
                input.setBackgroundResource(R.drawable.input_default);
            } catch (Exception e) {
                // ignore and just set edit-text error color and default result text
                setDefaultResultTextAndBackgroundResource(R.drawable.input_error);
            }

            if (input.getInput() == Input.TIME) {
                if (EasterEgg.TRIGGER_TEXT.equals(inputText)) {
                    runnersKeyboard.hide(); // hide immediately without delay
                    EasterEgg.show(interactionContainer.getContext());
                }
            }
        } else { // empty input is okay, but nothing to calculate, set default result text
            setDefaultResultTextAndBackgroundResource(R.drawable.input_default);
        }
        interactionContainer.toggleWidgets(input);
        runnersKeyboard.enableDeleteButton(!inputText.isEmpty());
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // Do nothing...
    }

    private void setDefaultResultTextAndBackgroundResource(int backgroundResource) {
        interactionContainer.setDefaultResults();
        input.setBackgroundResource(backgroundResource);
    }
}
