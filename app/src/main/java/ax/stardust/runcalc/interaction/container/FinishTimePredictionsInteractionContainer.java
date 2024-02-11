package ax.stardust.runcalc.interaction.container;

import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import ax.stardust.runcalc.R;
import ax.stardust.runcalc.component.keyboard.KeyboardHandler;
import ax.stardust.runcalc.component.keyboard.RunnersKeyboard;
import ax.stardust.runcalc.component.watcher.ReferencedRadioGroupWatcher;
import ax.stardust.runcalc.component.watcher.ReferencedTextWatcher;
import ax.stardust.runcalc.component.widget.KeyboardlessEditText;
import ax.stardust.runcalc.function.PredictionDistance;
import ax.stardust.runcalc.function.Property;
import ax.stardust.runcalc.pojo.FinishTimePredictions;

public class FinishTimePredictionsInteractionContainer implements InteractionContainer {
    private Context context;
    private Property property;
    private RunnersKeyboard keyboard;
    private KeyboardlessEditText finishTimeInput;
    private RadioGroup distanceInputGroup;
    private RadioButton distance5kmInput;
    // omit the button for 10k, as it's the only other option besides 5k

    private TextView results100mTimeTextView;
    private TextView results100mPaceTextView;
    private TextView results200mTimeTextView;
    private TextView results200mPaceTextView;
    private TextView results400mTimeTextView;
    private TextView results400mPaceTextView;
    private TextView results800mTimeTextView;
    private TextView results800mPaceTextView;
    private TextView results1500mTimeTextView;
    private TextView results1500mPaceTextView;
    private TextView results1miTimeTextView;
    private TextView results1miPaceTextView;
    private TextView results2miTimeTextView;
    private TextView results2miPaceTextView;
    private TextView results5kmTimeTextView;
    private TextView results5kmPaceTextView;
    private TextView results5miTimeTextView;
    private TextView results5miPaceTextView;
    private TextView results10kmTimeTextView;
    private TextView results10kmPaceTextView;
    private TextView results10miTimeTextView;
    private TextView results10miPaceTextView;
    private TextView resultsHalfMarathonTimeTextView;
    private TextView resultsHalfMarathonPaceTextView;
    private TextView resultsMarathonTimeTextView;
    private TextView resultsMarathonPaceTextView;
    private TextView results50kmTimeTextView;
    private TextView results50kmPaceTextView;
    private TextView results50miTimeTextView;
    private TextView results50miPaceTextView;

    private FinishTimePredictionsInteractionContainer() {
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
        if (hasValidCalculationInput(finishTimeInput)) {
            String distanceAndTime = distance5kmInput.isChecked() ? "5k|" : "10k|";
            distanceAndTime += getTextOfInput(finishTimeInput);

            FinishTimePredictions finishTimePredictions = new GsonBuilder().create()
                    .fromJson(property.getCalculatorFunction().apply(distanceAndTime), FinishTimePredictions.class);

            setResults(results100mTimeTextView, results100mPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_100_M).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_100_M).getPace());
            setResults(results200mTimeTextView, results200mPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_200_M).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_200_M).getPace());
            setResults(results400mTimeTextView, results400mPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_400_M).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_400_M).getPace());
            setResults(results800mTimeTextView, results800mPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_800_M).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_800_M).getPace());
            setResults(results1500mTimeTextView, results1500mPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_1500_M).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_1500_M).getPace());
            setResults(results1miTimeTextView, results1miPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_1_MI).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_1_MI).getPace());
            setResults(results2miTimeTextView, results2miPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_2_MI).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_2_MI).getPace());
            setResults(results5kmTimeTextView, results5kmPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_5_K).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_5_K).getPace());
            setResults(results5miTimeTextView, results5miPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_5_MI).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_5_MI).getPace());
            setResults(results10kmTimeTextView, results10kmPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_10_K).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_10_K).getPace());
            setResults(results10miTimeTextView, results10miPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_10_MI).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_10_MI).getPace());
            setResults(resultsHalfMarathonTimeTextView, resultsHalfMarathonPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_HALF_MARATHON).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_HALF_MARATHON).getPace());
            setResults(resultsMarathonTimeTextView, resultsMarathonPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_MARATHON).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_MARATHON).getPace());
            setResults(results50kmTimeTextView, results50kmPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_50_K).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_50_K).getPace());
            setResults(results50miTimeTextView, results50miPaceTextView, finishTimePredictions.getPrediction(PredictionDistance.D_50_MI).getTime(), finishTimePredictions.getPrediction(PredictionDistance.D_50_MI).getPace());
        }
    }

    @Override
    public void setListeners() {
        finishTimeInput.addTextChangedListener(new ReferencedTextWatcher(this, finishTimeInput, keyboard));
        finishTimeInput.setOnFocusChangeListener(new KeyboardHandler(keyboard));
        finishTimeInput.setOnTouchListener(new KeyboardHandler(keyboard));
        distanceInputGroup.setOnCheckedChangeListener(new ReferencedRadioGroupWatcher(this));
    }

    @Override
    public void setDefaultResults() {
        setResults(results100mTimeTextView, results100mPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(results200mTimeTextView, results200mPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(results400mTimeTextView, results400mPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(results800mTimeTextView, results800mPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(results1500mTimeTextView, results1500mPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(results1miTimeTextView, results1miPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(results2miTimeTextView, results2miPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(results5kmTimeTextView, results5kmPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(results5miTimeTextView, results5miPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(results10kmTimeTextView, results10kmPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(results10miTimeTextView, results10miPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(resultsHalfMarathonTimeTextView, resultsHalfMarathonPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(resultsMarathonTimeTextView, resultsMarathonPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(results50kmTimeTextView, results50kmPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
        setResults(results50miTimeTextView, results50miPaceTextView, context.getString(R.string.default_distance_pace), context.getString(R.string.default_distance_pace));
    }

    private void setResults(TextView timeResultsTextView, TextView paceResultsTextView, String time, String pace) {
        timeResultsTextView.setText(time);
        paceResultsTextView.setText(pace);
    }

    @Override
    public int compareTo(@NonNull InteractionContainer that) {
        return this.property.compareTo(that.getProperty());
    }

    public static class Builder {
        private final Context context;
        private Property property;
        private RunnersKeyboard keyboard;
        private KeyboardlessEditText finishTimeInput;
        private RadioGroup distanceInputGroup;
        private RadioButton distance5kmInput;

        private TextView results100mTimeTextView;
        private TextView results100mPaceTextView;
        private TextView results200mTimeTextView;
        private TextView results200mPaceTextView;
        private TextView results400mTimeTextView;
        private TextView results400mPaceTextView;
        private TextView results800mTimeTextView;
        private TextView results800mPaceTextView;
        private TextView results1500mTimeTextView;
        private TextView results1500mPaceTextView;
        private TextView results1miTimeTextView;
        private TextView results1miPaceTextView;
        private TextView results2miTimeTextView;
        private TextView results2miPaceTextView;
        private TextView results5kmTimeTextView;
        private TextView results5kmPaceTextView;
        private TextView results5miTimeTextView;
        private TextView results5miPaceTextView;
        private TextView results10kmTimeTextView;
        private TextView results10kmPaceTextView;
        private TextView results10miTimeTextView;
        private TextView results10miPaceTextView;
        private TextView resultsHalfMarathonTimeTextView;
        private TextView resultsHalfMarathonPaceTextView;
        private TextView resultsMarathonTimeTextView;
        private TextView resultsMarathonPaceTextView;
        private TextView results50kmTimeTextView;
        private TextView results50kmPaceTextView;
        private TextView results50miTimeTextView;
        private TextView results50miPaceTextView;

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

        public Builder setFinishTimeInput(KeyboardlessEditText finishTimeInput) {
            this.finishTimeInput = finishTimeInput;
            return this;
        }

        public Builder setDistanceInputGroup(RadioGroup distanceInputGroup) {
            this.distanceInputGroup = distanceInputGroup;
            return this;
        }

        public Builder setDistance5kmInput(RadioButton distance5kmInput) {
            this.distance5kmInput = distance5kmInput;
            return this;
        }

        public Builder setResults100mTimeTextView(TextView results100mTimeTextView) {
            this.results100mTimeTextView = results100mTimeTextView;
            return this;
        }

        public Builder setResults100mPaceTextView(TextView results100mPaceTextView) {
            this.results100mPaceTextView = results100mPaceTextView;
            return this;
        }

        public Builder setResults200mTimeTextView(TextView results200mTimeTextView) {
            this.results200mTimeTextView = results200mTimeTextView;
            return this;
        }

        public Builder setResults200mPaceTextView(TextView results200mPaceTextView) {
            this.results200mPaceTextView = results200mPaceTextView;
            return this;
        }

        public Builder setResults400mTimeTextView(TextView results400mTimeTextView) {
            this.results400mTimeTextView = results400mTimeTextView;
            return this;
        }

        public Builder setResults400mPaceTextView(TextView results400mPaceTextView) {
            this.results400mPaceTextView = results400mPaceTextView;
            return this;
        }

        public Builder setResults800mTimeTextView(TextView results800mTimeTextView) {
            this.results800mTimeTextView = results800mTimeTextView;
            return this;
        }

        public Builder setResults800mPaceTextView(TextView results800mPaceTextView) {
            this.results800mPaceTextView = results800mPaceTextView;
            return this;
        }

        public Builder setResults1500mTimeTextView(TextView results1500mTimeTextView) {
            this.results1500mTimeTextView = results1500mTimeTextView;
            return this;
        }

        public Builder setResults1500mPaceTextView(TextView results1500mPaceTextView) {
            this.results1500mPaceTextView = results1500mPaceTextView;
            return this;
        }

        public Builder setResults1miTimeTextView(TextView results1miTimeTextView) {
            this.results1miTimeTextView = results1miTimeTextView;
            return this;
        }

        public Builder setResults1miPaceTextView(TextView results1miPaceTextView) {
            this.results1miPaceTextView = results1miPaceTextView;
            return this;
        }

        public Builder setResults2miTimeTextView(TextView results2miTimeTextView) {
            this.results2miTimeTextView = results2miTimeTextView;
            return this;
        }

        public Builder setResults2miPaceTextView(TextView results2miPaceTextView) {
            this.results2miPaceTextView = results2miPaceTextView;
            return this;
        }

        public Builder setResults5kmTimeTextView(TextView results5kmTimeTextView) {
            this.results5kmTimeTextView = results5kmTimeTextView;
            return this;
        }

        public Builder setResults5kmPaceTextView(TextView results5kmPaceTextView) {
            this.results5kmPaceTextView = results5kmPaceTextView;
            return this;
        }

        public Builder setResults5miTimeTextView(TextView results5miTimeTextView) {
            this.results5miTimeTextView = results5miTimeTextView;
            return this;
        }

        public Builder setResults5miPaceTextView(TextView results5miPaceTextView) {
            this.results5miPaceTextView = results5miPaceTextView;
            return this;
        }

        public Builder setResults10kmTimeTextView(TextView results10kmTimeTextView) {
            this.results10kmTimeTextView = results10kmTimeTextView;
            return this;
        }

        public Builder setResults10kmPaceTextView(TextView results10kmPaceTextView) {
            this.results10kmPaceTextView = results10kmPaceTextView;
            return this;
        }

        public Builder setResults10miTimeTextView(TextView results10miTimeTextView) {
            this.results10miTimeTextView = results10miTimeTextView;
            return this;
        }

        public Builder setResults10miPaceTextView(TextView results10miPaceTextView) {
            this.results10miPaceTextView = results10miPaceTextView;
            return this;
        }

        public Builder setResultsHalfMarathonTimeTextView(TextView resultsHalfMarathonTimeTextView) {
            this.resultsHalfMarathonTimeTextView = resultsHalfMarathonTimeTextView;
            return this;
        }

        public Builder setResultsHalfMarathonPaceTextView(TextView resultsHalfMarathonPaceTextView) {
            this.resultsHalfMarathonPaceTextView = resultsHalfMarathonPaceTextView;
            return this;
        }

        public Builder setResultsMarathonTimeTextView(TextView resultsMarathonTimeTextView) {
            this.resultsMarathonTimeTextView = resultsMarathonTimeTextView;
            return this;
        }

        public Builder setResultsMarathonPaceTextView(TextView resultsMarathonPaceTextView) {
            this.resultsMarathonPaceTextView = resultsMarathonPaceTextView;
            return this;
        }

        public Builder setResults50kmTimeTextView(TextView results50kmTimeTextView) {
            this.results50kmTimeTextView = results50kmTimeTextView;
            return this;
        }

        public Builder setResults50kmPaceTextView(TextView results50kmPaceTextView) {
            this.results50kmPaceTextView = results50kmPaceTextView;
            return this;
        }

        public Builder setResults50miTimeTextView(TextView results50miTimeTextView) {
            this.results50miTimeTextView = results50miTimeTextView;
            return this;
        }

        public Builder setResults50miPaceTextView(TextView results50miPaceTextView) {
            this.results50miPaceTextView = results50miPaceTextView;
            return this;
        }

        public FinishTimePredictionsInteractionContainer build() {
            FinishTimePredictionsInteractionContainer interactionContainer = new FinishTimePredictionsInteractionContainer();
            interactionContainer.context = this.context;
            interactionContainer.property = this.property;
            interactionContainer.keyboard = this.keyboard;
            interactionContainer.finishTimeInput = this.finishTimeInput;
            interactionContainer.distanceInputGroup = this.distanceInputGroup;
            interactionContainer.distance5kmInput = this.distance5kmInput;
            interactionContainer.results100mTimeTextView = this.results100mTimeTextView;
            interactionContainer.results100mPaceTextView = this.results100mPaceTextView;
            interactionContainer.results200mTimeTextView = this.results200mTimeTextView;
            interactionContainer.results200mPaceTextView = this.results200mPaceTextView;
            interactionContainer.results400mTimeTextView = this.results400mTimeTextView;
            interactionContainer.results400mPaceTextView = this.results400mPaceTextView;
            interactionContainer.results800mTimeTextView = this.results800mTimeTextView;
            interactionContainer.results800mPaceTextView = this.results800mPaceTextView;
            interactionContainer.results1500mTimeTextView = this.results1500mTimeTextView;
            interactionContainer.results1500mPaceTextView = this.results1500mPaceTextView;
            interactionContainer.results1miTimeTextView = this.results1miTimeTextView;
            interactionContainer.results1miPaceTextView = this.results1miPaceTextView;
            interactionContainer.results2miTimeTextView = this.results2miTimeTextView;
            interactionContainer.results2miPaceTextView = this.results2miPaceTextView;
            interactionContainer.results5kmTimeTextView = this.results5kmTimeTextView;
            interactionContainer.results5kmPaceTextView = this.results5kmPaceTextView;
            interactionContainer.results5miTimeTextView = this.results5miTimeTextView;
            interactionContainer.results5miPaceTextView = this.results5miPaceTextView;
            interactionContainer.results10kmTimeTextView = this.results10kmTimeTextView;
            interactionContainer.results10kmPaceTextView = this.results10kmPaceTextView;
            interactionContainer.results10miTimeTextView = this.results10miTimeTextView;
            interactionContainer.results10miPaceTextView = this.results10miPaceTextView;
            interactionContainer.resultsHalfMarathonTimeTextView = this.resultsHalfMarathonTimeTextView;
            interactionContainer.resultsHalfMarathonPaceTextView = this.resultsHalfMarathonPaceTextView;
            interactionContainer.resultsMarathonTimeTextView = this.resultsMarathonTimeTextView;
            interactionContainer.resultsMarathonPaceTextView = this.resultsMarathonPaceTextView;
            interactionContainer.results50kmTimeTextView = this.results50kmTimeTextView;
            interactionContainer.results50kmPaceTextView = this.results50kmPaceTextView;
            interactionContainer.results50miTimeTextView = this.results50miTimeTextView;
            interactionContainer.results50miPaceTextView = this.results50miPaceTextView;
            return interactionContainer;
        }
    }
}
