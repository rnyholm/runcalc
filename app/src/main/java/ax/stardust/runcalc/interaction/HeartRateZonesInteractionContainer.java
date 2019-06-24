package ax.stardust.runcalc.interaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import ax.stardust.runcalc.R;
import ax.stardust.runcalc.component.KeyboardHandler;
import ax.stardust.runcalc.component.KeyboardlessEditText;
import ax.stardust.runcalc.component.ReferencedSwitchWatcher;
import ax.stardust.runcalc.component.ReferencedTextWatcher;
import ax.stardust.runcalc.component.RunnersKeyboard;
import ax.stardust.runcalc.input.Property;
import ax.stardust.runcalc.pojo.HeartRateZones;

public class HeartRateZonesInteractionContainer implements InteractionContainer, Comparable {
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
    public void calculateIfPossible() {
        if ((hasValidCalculationInput(maximumHeartRateInput) || hasValidCalculationInput(ageInput))
                && hasValidCalculationInput(restingHeartRateInput)) {
            String hrcd = (experiencedRunnerInput.isChecked() ? "E" : "B") + "|" + getTextOfInput(restingHeartRateInput);
            if (hasValidCalculationInput(maximumHeartRateInput)) {
                hrcd += "|HR-" + getTextOfInput(maximumHeartRateInput);
            } else {
                hrcd += "|A-" + getTextOfInput(ageInput);
            }

            HeartRateZones heartRateZones = new GsonBuilder().create()
                    .fromJson(property.getCalculatorFunction().apply(hrcd), HeartRateZones.class);

            setResult(heartRateZone1ResultsTextView, context.getString(R.string.hr_zone_1) + context.getString(R.string.hr_zone_results), heartRateZones.getZone(HeartRateZones.ZONE_1));
            setResult(heartRateZone2ResultsTextView, context.getString(R.string.hr_zone_2) + context.getString(R.string.hr_zone_results), heartRateZones.getZone(HeartRateZones.ZONE_2));
            setResult(heartRateZone3ResultsTextView, context.getString(R.string.hr_zone_3) + context.getString(R.string.hr_zone_results), heartRateZones.getZone(HeartRateZones.ZONE_3));
            setResult(heartRateZone4ResultsTextView, context.getString(R.string.hr_zone_4) + context.getString(R.string.hr_zone_results), heartRateZones.getZone(HeartRateZones.ZONE_4));
            setResult(heartRateZone5ResultsTextView, context.getString(R.string.hr_zone_5) + context.getString(R.string.hr_zone_results), heartRateZones.getZone(HeartRateZones.ZONE_5));
        }
    }

    @Override
    public void setDefaultResults() {
        heartRateZone1ResultsTextView.setText(context.getString(R.string.hr_zone_1));
        heartRateZone2ResultsTextView.setText(context.getString(R.string.hr_zone_2));
        heartRateZone3ResultsTextView.setText(context.getString(R.string.hr_zone_3));
        heartRateZone4ResultsTextView.setText(context.getString(R.string.hr_zone_4));
        heartRateZone5ResultsTextView.setText(context.getString(R.string.hr_zone_5));
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

    private void setResult(TextView resultsTextView, String resultsText, HeartRateZones.HeartRateZone heartRateZone) {
        resultsText = resultsText.replace("{hr-range}", heartRateZone.getHrRange());
        resultsText = resultsText.replace("{hr-range-percentage}", heartRateZone.getPercentageRange());
        resultsTextView.setText(resultsText);
    }

    @Override
    public int compareTo(@NonNull Object that) {
        return this.property.compareTo(((InteractionContainer) that).getProperty());
    }

    public static class Builder {
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
