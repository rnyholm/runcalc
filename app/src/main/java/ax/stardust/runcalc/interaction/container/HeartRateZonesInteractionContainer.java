package ax.stardust.runcalc.interaction.container;

import android.widget.Switch;
import android.widget.TextView;

import ax.stardust.runcalc.component.KeyboardlessEditText;
import ax.stardust.runcalc.input.Property;

public class HeartRateZonesInteractionContainer {
    private Property property;
    private KeyboardlessEditText maximumHeartRateInput;
    private KeyboardlessEditText restingHeartRateInput;
    private KeyboardlessEditText ageInput;
    private Switch experiencedRunnerInput;

    private TextView heartRateZone1Results;
    private TextView heartRateZone2Results;
    private TextView heartRateZone3Results;
    private TextView heartRateZone4Results;
    private TextView heartRateZone5Results;

    private HeartRateZonesInteractionContainer() {
    }

    public class Builder {
        private Property property;
        private KeyboardlessEditText maximumHeartRateInput;
        private KeyboardlessEditText restingHeartRateInput;
        private KeyboardlessEditText ageInput;
        private Switch experiencedRunnerInput;

        private TextView heartRateZone1Results;
        private TextView heartRateZone2Results;
        private TextView heartRateZone3Results;
        private TextView heartRateZone4Results;
        private TextView heartRateZone5Results;

        public Builder(Property property) {
            this.property = property;
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

        public Builder setHeartRateZone1Results(TextView heartRateZone1Results) {
            this.heartRateZone1Results = heartRateZone1Results;
            return this;
        }

        public Builder setHeartRateZone2Results(TextView heartRateZone2Results) {
            this.heartRateZone2Results = heartRateZone2Results;
            return this;
        }

        public Builder setHeartRateZone3Results(TextView heartRateZone3Results) {
            this.heartRateZone3Results = heartRateZone3Results;
            return this;
        }

        public Builder setHeartRateZone4Results(TextView heartRateZone4Results) {
            this.heartRateZone4Results = heartRateZone4Results;
            return this;
        }

        public Builder setHeartRateZone5Results(TextView heartRateZone5Results) {
            this.heartRateZone5Results = heartRateZone5Results;
            return this;
        }

        public HeartRateZonesInteractionContainer build() {
            HeartRateZonesInteractionContainer interactionContainer = new HeartRateZonesInteractionContainer();
            interactionContainer.property = this.property;
            interactionContainer.maximumHeartRateInput = this.maximumHeartRateInput;
            interactionContainer.restingHeartRateInput = this.restingHeartRateInput;
            interactionContainer.ageInput = this.ageInput;
            interactionContainer.experiencedRunnerInput = this.experiencedRunnerInput;
            interactionContainer.heartRateZone1Results = this.heartRateZone1Results;
            interactionContainer.heartRateZone2Results = this.heartRateZone2Results;
            interactionContainer.heartRateZone3Results = this.heartRateZone3Results;
            interactionContainer.heartRateZone4Results = this.heartRateZone4Results;
            interactionContainer.heartRateZone5Results = this.heartRateZone5Results;
            return interactionContainer;
        }
    }
}
