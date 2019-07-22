package ax.stardust.runcalc.interaction.container;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import ax.stardust.runcalc.R;
import ax.stardust.runcalc.activity.RunnersCalculator;
import ax.stardust.runcalc.component.keyboard.KeyboardHandler;
import ax.stardust.runcalc.component.widget.KeyboardlessEditText;
import ax.stardust.runcalc.component.watcher.ReferencedTextWatcher;
import ax.stardust.runcalc.component.keyboard.RunnersKeyboard;
import ax.stardust.runcalc.function.Property;

public class DualInputInteractionContainer implements InteractionContainer {
    private Context context;
    private Property property;
    private RunnersKeyboard keyboard;
    private KeyboardlessEditText firstInput;
    private KeyboardlessEditText secondInput;
    private TextView resultsTextView;
    private int resultsTextID; // of results text

    private DualInputInteractionContainer() {
    }

    @Override
    public Property getProperty() {
        return property;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void calculateIfPossible() {
        if (hasValidCalculationInput(firstInput) &&
                hasValidCalculationInput(secondInput)) {
            String result = property.getCalculatorFunction().apply(getCombinedText());
            setResult(result);
        }
    }

    @Override
    public void setDefaultResults() {
        switch (property) {
            case CALCULATE_PACE:
                setResult(context.getString(R.string.default_pace));
                break;
            case CALCULATE_TIME:
                setResult(context.getString(R.string.default_time));
                break;
            case CALCULATE_DISTANCE:
                setResult(context.getString(R.string.default_speed_distance_km));
        }
    }

    @Override
    public void setListeners() {
        firstInput.addTextChangedListener(new ReferencedTextWatcher(this, firstInput, keyboard));
        firstInput.setOnFocusChangeListener(new KeyboardHandler(keyboard));
        firstInput.setOnTouchListener(new KeyboardHandler(keyboard));
        secondInput.addTextChangedListener(new ReferencedTextWatcher(this, secondInput, keyboard));
        secondInput.setOnFocusChangeListener(new KeyboardHandler(keyboard));
        secondInput.setOnTouchListener(new KeyboardHandler(keyboard));
    }

    private void setResult(String result) {
        String text = context.getString(resultsTextID);
        text = text.replace("{pace}", RunnersCalculator.pace);
        text = text.replace("{distance}", RunnersCalculator.distance);
        text = text.replace("{result}", result);
        resultsTextView.setText(text);
    }

    private String getCombinedText() {
        String combinedText = getFirstInputText();
        String secondInputText = getSecondInputText();

        if (!secondInputText.isEmpty()) {
            combinedText += "|" + secondInputText;
        }

        return combinedText;
    }

    private String getFirstInputText() {
        return getTextOfInput(firstInput);
    }

    private String getSecondInputText() {
        return getTextOfInput(secondInput);
    }

    @Override
    public int compareTo(@NonNull Object that) {
        return this.property.compareTo(((InteractionContainer) that).getProperty());
    }

    public static class Builder {
        private final Context context;
        private Property property;
        private RunnersKeyboard keyboard;
        private KeyboardlessEditText firstInput;
        private KeyboardlessEditText secondInput;
        private TextView resultsTextView;
        private int resultsTextID;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setProperty(Property property) {
            this.property = property;
            return this;
        }

        public Builder setKeyboard(RunnersKeyboard keyboard) {
            this.keyboard = keyboard;
            return this;
        }

        public Builder setFirstInput(KeyboardlessEditText firstInput) {
            this.firstInput = firstInput;
            return this;
        }

        public Builder setSecondInput(KeyboardlessEditText secondInput) {
            this.secondInput = secondInput;
            return this;
        }

        public Builder setResultsTextView(TextView resultsTextView) {
            this.resultsTextView = resultsTextView;
            return this;
        }

        public Builder setResultsTextID(int resultsTextID) {
            this.resultsTextID = resultsTextID;
            return this;
        }

        public DualInputInteractionContainer build() {
            DualInputInteractionContainer interactionContainer = new DualInputInteractionContainer();
            interactionContainer.context = this.context;
            interactionContainer.property = this.property;
            interactionContainer.keyboard = this.keyboard;
            interactionContainer.firstInput = this.firstInput;
            interactionContainer.secondInput = this.secondInput;
            interactionContainer.resultsTextView = this.resultsTextView;
            interactionContainer.resultsTextID = this.resultsTextID;
            return interactionContainer;
        }
    }
}