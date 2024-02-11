package ax.stardust.runcalc.interaction.container;

import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import ax.stardust.runcalc.R;
import ax.stardust.runcalc.component.keyboard.KeyboardHandler;
import ax.stardust.runcalc.component.keyboard.RunnersKeyboard;
import ax.stardust.runcalc.component.watcher.ReferencedSwitchWatcher;
import ax.stardust.runcalc.component.watcher.ReferencedTextWatcher;
import ax.stardust.runcalc.component.widget.KeyboardlessEditText;
import ax.stardust.runcalc.function.Property;
import ax.stardust.runcalc.pojo.HeartRateZones;

public class HeartRateZonesInteractionContainer implements InteractionContainer {
    private Context context;
    private Property property;
    private RunnersKeyboard keyboard;
    private KeyboardlessEditText maximumHeartRateInput;
    private KeyboardlessEditText restingHeartRateInput;
    private KeyboardlessEditText ageInput;
    private Switch experiencedRunnerInput;

    private TextView heartRateZone1ResultsTextView;
    private TextView heartRateZone2ResultsTextView;
    private TextView heartRateZone3ResultsTextView;
    private TextView heartRateZone4ResultsTextView;
    private TextView heartRateZone5ResultsTextView;

    private HeartRateZonesInteractionContainer() {
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
        if ((hasValidCalculationInput(maximumHeartRateInput) || hasValidCalculationInput(ageInput))
                && hasValidCalculationInput(restingHeartRateInput)) {
            String heartRateCalculationData = (experiencedRunnerInput.isChecked() ? "E" : "B") + "|" + getTextOfInput(restingHeartRateInput);
            if (hasValidCalculationInput(maximumHeartRateInput)) {
                heartRateCalculationData += "|HR-" + getTextOfInput(maximumHeartRateInput);
            } else {
                heartRateCalculationData += "|A-" + getTextOfInput(ageInput);
            }

            HeartRateZones heartRateZones = new GsonBuilder().create()
                    .fromJson(property.getCalculatorFunction().apply(heartRateCalculationData), HeartRateZones.class);

            setResult(heartRateZone1ResultsTextView, context.getString(R.string.heart_rate_zone_1) + context.getString(R.string.hr_zone_results), heartRateZones.getZone(HeartRateZones.ZONE_1));
            setResult(heartRateZone2ResultsTextView, context.getString(R.string.heart_rate_zone_2) + context.getString(R.string.hr_zone_results), heartRateZones.getZone(HeartRateZones.ZONE_2));
            setResult(heartRateZone3ResultsTextView, context.getString(R.string.heart_rate_zone_3) + context.getString(R.string.hr_zone_results), heartRateZones.getZone(HeartRateZones.ZONE_3));
            setResult(heartRateZone4ResultsTextView, context.getString(R.string.heart_rate_zone_4) + context.getString(R.string.hr_zone_results), heartRateZones.getZone(HeartRateZones.ZONE_4));
            setResult(heartRateZone5ResultsTextView, context.getString(R.string.heart_rate_zone_5) + context.getString(R.string.hr_zone_results), heartRateZones.getZone(HeartRateZones.ZONE_5));
        }
    }

    private void setResult(TextView resultsTextView, String resultsText, HeartRateZones.HeartRateZone heartRateZone) {
        resultsText = resultsText.replace("{hr-range}", heartRateZone.getHrRange());
        resultsText = resultsText.replace("{hr-range-percentage}", heartRateZone.getPercentageRange());
        resultsTextView.setText(resultsText);
    }

    @Override
    public void setDefaultResults() {
        heartRateZone1ResultsTextView.setText(context.getString(R.string.heart_rate_zone_1));
        heartRateZone2ResultsTextView.setText(context.getString(R.string.heart_rate_zone_2));
        heartRateZone3ResultsTextView.setText(context.getString(R.string.heart_rate_zone_3));
        heartRateZone4ResultsTextView.setText(context.getString(R.string.heart_rate_zone_4));
        heartRateZone5ResultsTextView.setText(context.getString(R.string.heart_rate_zone_5));
    }

    @Override
    public void setListeners() {
        maximumHeartRateInput.addTextChangedListener(new ReferencedTextWatcher(this, maximumHeartRateInput, keyboard));
        maximumHeartRateInput.setOnFocusChangeListener(new KeyboardHandler(keyboard));
        maximumHeartRateInput.setOnTouchListener(new KeyboardHandler(keyboard));
        restingHeartRateInput.addTextChangedListener(new ReferencedTextWatcher(this, restingHeartRateInput, keyboard));
        restingHeartRateInput.setOnFocusChangeListener(new KeyboardHandler(keyboard));
        restingHeartRateInput.setOnTouchListener(new KeyboardHandler(keyboard));
        ageInput.addTextChangedListener(new ReferencedTextWatcher(this, ageInput, keyboard));
        ageInput.setOnFocusChangeListener(new KeyboardHandler(keyboard));
        ageInput.setOnTouchListener(new KeyboardHandler(keyboard));
        experiencedRunnerInput.setOnCheckedChangeListener(new ReferencedSwitchWatcher(this));
    }

    @Override
    public void toggleWidgets(KeyboardlessEditText input) {
        if (input != null) {
            String text = getTextOfInput(input);
            if (input == maximumHeartRateInput) {
                ageInput.setEnabled(text.isEmpty());
                toggleBackgroundResource(!text.isEmpty(), ageInput);
            } else if (input == ageInput) {
                maximumHeartRateInput.setEnabled(text.isEmpty());
                toggleBackgroundResource(!text.isEmpty(), maximumHeartRateInput);
            }
        }
    }

    private void toggleBackgroundResource(boolean hasText, KeyboardlessEditText input) {
        if (hasText) {
            input.setBackgroundResource(R.drawable.input_disabled);
        } else {
            input.setBackgroundResource(R.drawable.input_default);
        }
    }

    @Override
    public int compareTo(@NonNull Object that) {
        return this.property.compareTo(((InteractionContainer) that).getProperty());
    }

    public static class Builder {
        private final Context context;
        private Property property;
        private RunnersKeyboard keyboard;
        private KeyboardlessEditText maximumHeartRateInput;
        private KeyboardlessEditText restingHeartRateInput;
        private KeyboardlessEditText ageInput;
        private Switch experiencedRunnerInput;

        private TextView heartRateZone1ResultsTextView;
        private TextView heartRateZone2ResultsTextView;
        private TextView heartRateZone3ResultsTextView;
        private TextView heartRateZone4ResultsTextView;
        private TextView heartRateZone5ResultsTextView;

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

        public Builder setMaximumHeartRateInput(KeyboardlessEditText maximumHeartRateInput) {
            this.maximumHeartRateInput = maximumHeartRateInput;
            return this;
        }

        public Builder setRestingHeartRateInput(KeyboardlessEditText restingHeartRateInput) {
            this.restingHeartRateInput = restingHeartRateInput;
            return this;
        }

        public Builder setAgeInput(KeyboardlessEditText ageInput) {
            this.ageInput = ageInput;
            return this;
        }

        public Builder setExperiencedRunnerInput(Switch experiencedRunnerInput) {
            this.experiencedRunnerInput = experiencedRunnerInput;
            return this;
        }

        public Builder setHeartRateZone1ResultsTextView(TextView heartRateZone1ResultsTextView) {
            this.heartRateZone1ResultsTextView = heartRateZone1ResultsTextView;
            return this;
        }

        public Builder setHeartRateZone2ResultsTextView(TextView heartRateZone2ResultsTextView) {
            this.heartRateZone2ResultsTextView = heartRateZone2ResultsTextView;
            return this;
        }

        public Builder setHeartRateZone3ResultsTextView(TextView heartRateZone3ResultsTextView) {
            this.heartRateZone3ResultsTextView = heartRateZone3ResultsTextView;
            return this;
        }

        public Builder setHeartRateZone4ResultsTextView(TextView heartRateZone4ResultsTextView) {
            this.heartRateZone4ResultsTextView = heartRateZone4ResultsTextView;
            return this;
        }

        public Builder setHeartRateZone5ResultsTextView(TextView heartRateZone5ResultsTextView) {
            this.heartRateZone5ResultsTextView = heartRateZone5ResultsTextView;
            return this;
        }

        public HeartRateZonesInteractionContainer build() {
            HeartRateZonesInteractionContainer interactionContainer = new HeartRateZonesInteractionContainer();
            interactionContainer.context = this.context;
            interactionContainer.property = this.property;
            interactionContainer.keyboard = this.keyboard;
            interactionContainer.maximumHeartRateInput = this.maximumHeartRateInput;
            interactionContainer.restingHeartRateInput = this.restingHeartRateInput;
            interactionContainer.ageInput = this.ageInput;
            interactionContainer.experiencedRunnerInput = this.experiencedRunnerInput;
            interactionContainer.heartRateZone1ResultsTextView = this.heartRateZone1ResultsTextView;
            interactionContainer.heartRateZone2ResultsTextView = this.heartRateZone2ResultsTextView;
            interactionContainer.heartRateZone3ResultsTextView = this.heartRateZone3ResultsTextView;
            interactionContainer.heartRateZone4ResultsTextView = this.heartRateZone4ResultsTextView;
            interactionContainer.heartRateZone5ResultsTextView = this.heartRateZone5ResultsTextView;
            return interactionContainer;
        }
    }
}
