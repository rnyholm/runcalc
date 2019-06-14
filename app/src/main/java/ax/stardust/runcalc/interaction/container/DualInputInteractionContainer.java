package ax.stardust.runcalc.interaction.container;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import ax.stardust.runcalc.R;
import ax.stardust.runcalc.activity.RunnersCalculator;
import ax.stardust.runcalc.component.KeyboardlessEditText;
import ax.stardust.runcalc.component.RunnersKeyboard;
import ax.stardust.runcalc.input.Property;

public class DualInputInteractionContainer implements InteractionContainer, Comparable {
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
                setResult(context.getString(R.string.default_speed_distance));
        }
    }

    @Override
    public void setListeners() {
        firstInput.addTextChangedListener(new RunnersCalculator.ReferencedTextWatcher(this, firstInput, keyboard));
        firstInput.setOnFocusChangeListener(new RunnersCalculator.KeyboardHandler(keyboard));
        firstInput.setOnTouchListener(new RunnersCalculator.KeyboardHandler(keyboard));
        secondInput.addTextChangedListener(new RunnersCalculator.ReferencedTextWatcher(this, secondInput, keyboard));
        secondInput.setOnFocusChangeListener(new RunnersCalculator.KeyboardHandler(keyboard));
        secondInput.setOnTouchListener(new RunnersCalculator.KeyboardHandler(keyboard));
    }

    private void setResult(String result) {
        String text = context.getString(resultsTextID);
        text = text.replace("{pace}", RunnersCalculator.pace);
        text = text.replace("{distance}", RunnersCalculator.distance);
        text = text.replace("{result}", result);
        resultsTextView.setText(text);
    }

    private boolean hasValidCalculationInput(KeyboardlessEditText input) {
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
    public boolean equals(Object that) {
        if (that != null) {
            if (DualInputInteractionContainer.class.isAssignableFrom(that.getClass())) {
                return this.property.equals(((DualInputInteractionContainer) that).getProperty());
            }
        }
        return false;
    }

    @Override
    public int compareTo(@NonNull Object that) {
        return this.property.compareTo(((DualInputInteractionContainer) that).getProperty());
    }

    public static class Builder {
        private Context context;
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