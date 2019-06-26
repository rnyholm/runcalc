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

public class SingleInputInteractionContainer implements InteractionContainer {
    private Context context;
    private Property property;
    private RunnersKeyboard keyboard;
    private KeyboardlessEditText input;
    private TextView resultsTextView;
    private int resultsTextID; // of result text

    private SingleInputInteractionContainer() {
    }

    @Override
    public Property getProperty() {
        return property;
    }

    @Override
    public void calculateIfPossible() {
        String result = property.getCalculatorFunction().apply(getTextOfInput(input));
        setResult(result);
    }

    @Override
    public void setDefaultResults() {
        switch (property) {
            case CONVERT_PACE_TO_SPEED:
                setResult(context.getString(R.string.default_speed_distance_km));
                break;
            case CONVERT_SPEED_TO_PACE:
                setResult(context.getString(R.string.default_pace));
                break;
            case CALCULATE_VO2MAX_ESTIMATE:
                setResult(context.getString(R.string.default_vo2max));
        }
    }

    @Override
    public void setListeners() {
        input.addTextChangedListener(new ReferencedTextWatcher(this, input, keyboard));
        input.setOnFocusChangeListener(new KeyboardHandler(keyboard));
        input.setOnTouchListener(new KeyboardHandler(keyboard));
    }

    private void setResult(String result) {
        String text = context.getString(resultsTextID);
        text = text.replace("{pace}", RunnersCalculator.pace);
        text = text.replace("{speed}", RunnersCalculator.speed);
        text = text.replace("{result}", result);
        resultsTextView.setText(text);
    }

    @Override
    public int compareTo(@NonNull Object that) {
        return this.property.compareTo(((InteractionContainer) that).getProperty());
    }

    public static class Builder {
        private final Context context;
        private Property property;
        private RunnersKeyboard keyboard;
        private KeyboardlessEditText input;
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

        public Builder setInput(KeyboardlessEditText input) {
            this.input = input;
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

        public SingleInputInteractionContainer build() {
            SingleInputInteractionContainer interactionContainer = new SingleInputInteractionContainer();
            interactionContainer.context = this.context;
            interactionContainer.property = this.property;
            interactionContainer.keyboard = this.keyboard;
            interactionContainer.input = this.input;
            interactionContainer.resultsTextView = this.resultsTextView;
            interactionContainer.resultsTextID = this.resultsTextID;
            return interactionContainer;
        }
    }
}
