package ax.stardust.runcalc.interaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import ax.stardust.runcalc.R;
import ax.stardust.runcalc.component.KeyboardHandler;
import ax.stardust.runcalc.component.KeyboardlessEditText;
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

    private int heartRateZone1ResultsTextID;
    private int heartRateZone2ResultsTextID;
    private int heartRateZone3ResultsTextID;
    private int heartRateZone4ResultsTextID;
    private int heartRateZone5ResultsTextID;

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
            // TODO: Take the switch into account
            String hrcd = "E" + "|" + getTextOfInput(restingHeartRateInput);
            if (hasValidCalculationInput(maximumHeartRateInput)) {
                hrcd += "|HR-" + getTextOfInput(maximumHeartRateInput);
            } else {
                hrcd += "|A-" + getTextOfInput(ageInput);
            }

            HeartRateZones heartRateZones = new GsonBuilder().create()
                    .fromJson(property.getCalculatorFunction().apply(hrcd), HeartRateZones.class);

            setResult(heartRateZone1ResultsTextView, heartRateZone1ResultsTextID, heartRateZones.getZone(HeartRateZones.ZONE_1));
            setResult(heartRateZone2ResultsTextView, heartRateZone2ResultsTextID, heartRateZones.getZone(HeartRateZones.ZONE_2));
            setResult(heartRateZone3ResultsTextView, heartRateZone3ResultsTextID, heartRateZones.getZone(HeartRateZones.ZONE_3));
            setResult(heartRateZone4ResultsTextView, heartRateZone4ResultsTextID, heartRateZones.getZone(HeartRateZones.ZONE_4));
            setResult(heartRateZone5ResultsTextView, heartRateZone5ResultsTextID, heartRateZones.getZone(HeartRateZones.ZONE_5));
        }
    }

    @Override
    public void setDefaultResults() {
        heartRateZone1ResultsTextView.setText(context.getString(R.string.hr_zone_1_default));
        heartRateZone2ResultsTextView.setText(context.getString(R.string.hr_zone_2_default));
        heartRateZone3ResultsTextView.setText(context.getString(R.string.hr_zone_3_default));
        heartRateZone4ResultsTextView.setText(context.getString(R.string.hr_zone_4_default));
        heartRateZone5ResultsTextView.setText(context.getString(R.string.hr_zone_5_default));
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

        // TODO: add listener for experienced switch
    }

    private void setResult(TextView resultsTextView, int resultsTextID, HeartRateZones.HeartRateZone heartRateZone) {
        String text = context.getString(resultsTextID);
        text = text.replace("{hr-range}", heartRateZone.getHrRange());
        text = text.replace("{hr-range-percentage}", heartRateZone.getPercentageRange());
        resultsTextView.setText(text);
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

        private int heartRateZone1ResultsTextID;
        private int heartRateZone2ResultsTextID;
        private int heartRateZone3ResultsTextID;
        private int heartRateZone4ResultsTextID;
        private int heartRateZone5ResultsTextID;

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

        public Builder setHeartRateZone1ResultsTextID(int heartRateZone1ResultsTextID) {
            this.heartRateZone1ResultsTextID = heartRateZone1ResultsTextID;
            return this;
        }

        public Builder setHeartRateZone2ResultsTextID(int heartRateZone2ResultsTextID) {
            this.heartRateZone2ResultsTextID = heartRateZone2ResultsTextID;
            return this;
        }

        public Builder setHeartRateZone3ResultsTextID(int heartRateZone3ResultsTextID) {
            this.heartRateZone3ResultsTextID = heartRateZone3ResultsTextID;
            return this;
        }

        public Builder setHeartRateZone4ResultsTextID(int heartRateZone4ResultsTextID) {
            this.heartRateZone4ResultsTextID = heartRateZone4ResultsTextID;
            return this;
        }

        public Builder setHeartRateZone5ResultsTextID(int heartRateZone5ResultsTextID) {
            this.heartRateZone5ResultsTextID = heartRateZone5ResultsTextID;
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
            interactionContainer.heartRateZone1ResultsTextID = this.heartRateZone1ResultsTextID;
            interactionContainer.heartRateZone2ResultsTextID = this.heartRateZone2ResultsTextID;
            interactionContainer.heartRateZone3ResultsTextID = this.heartRateZone3ResultsTextID;
            interactionContainer.heartRateZone4ResultsTextID = this.heartRateZone4ResultsTextID;
            interactionContainer.heartRateZone5ResultsTextID = this.heartRateZone5ResultsTextID;
            return interactionContainer;
        }
    }
}
