package ax.stardust.runcalc.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Set;
import java.util.TreeSet;

import ax.stardust.runcalc.R;
import ax.stardust.runcalc.component.KeyboardlessEditText;
import ax.stardust.runcalc.component.RunnersKeyboard;
import ax.stardust.runcalc.input.Input;
import ax.stardust.runcalc.input.Property;
import ax.stardust.runcalc.interaction.container.DualInputInteractionContainer;
import ax.stardust.runcalc.interaction.container.InteractionContainer;
import ax.stardust.runcalc.interaction.container.SingleInputInteractionContainer;
import ax.stardust.runcalc.util.Calculator;

public class RunnersCalculator extends AppCompatActivity {
    public static String pace;
    public static String speed;
    public static String distance;

    private final Set<InteractionContainer> interactionContainers = new TreeSet<>();
    private RunnersKeyboard runnersKeyboard;

    private TextView convertPaceToSpeedTextView;
    private TextView convertSpeedToPaceTextView;
    private TextView calculatePaceTextView;
    private TextView calculatePaceDistanceHintTextView;
    private TextView calculatePaceTimeHintTextView;
    private TextView calculateTimeTextView;
    private TextView calculateTimeDistanceHintTextView;
    private TextView calculateTimePaceHintTextView;
    private TextView calculateDistanceTextView;
    private TextView calculateDistanceTimeHintTextView;
    private TextView calculateDistancePaceHintTextView;
    private TextView calculateVO2maxEstimateTextView;
    private TextView versionNameTextView;

    private ImageView calculateVO2maxEstimateCooperTestLinkImageView;
    private ImageView calculateTrainingHeartRateZonesKarvonenLinkImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        findViews();
        setGlobalTexts();
        setTexts();
        setListeners();
    }

    @Override
    public void onBackPressed() {
        if (runnersKeyboard.getVisibility() == View.VISIBLE) {
            // hacky way to release focus from any edit text, by releasing it also the keyboard will be closed
            versionNameTextView.requestFocus();
        } else {
            super.onBackPressed();
        }
    }

    private void findViews() {
        runnersKeyboard = findViewById(R.id.soft_keyboard);

        convertPaceToSpeedTextView = findViewById(R.id.pace_to_speed_tv);
        convertSpeedToPaceTextView = findViewById(R.id.speed_to_pace_tv);
        calculatePaceTextView = findViewById(R.id.calculate_pace_tv);
        calculatePaceDistanceHintTextView = findViewById(R.id.calculate_pace_distance_hint_tv);
        calculatePaceTimeHintTextView = findViewById(R.id.calculate_pace_time_hint_tv);
        calculateTimeTextView = findViewById(R.id.calculate_time_tv);
        calculateTimeDistanceHintTextView = findViewById(R.id.calculate_time_distance_hint_tv);
        calculateTimePaceHintTextView = findViewById(R.id.calculate_time_pace_hint_tv);
        calculateDistanceTextView = findViewById(R.id.calculate_distance_tv);
        calculateDistanceTimeHintTextView = findViewById(R.id.calculate_distance_time_hint_tv);
        calculateDistancePaceHintTextView = findViewById(R.id.calculate_distance_pace_hint_tv);
        calculateVO2maxEstimateTextView = findViewById(R.id.calculate_vo2max_estimate_tv);
        versionNameTextView = findViewById(R.id.version_name_tv);

        KeyboardlessEditText paceToSpeedEditText = findViewById(R.id.pace_to_speed_et);
        paceToSpeedEditText.setInput(Input.PACE);
        paceToSpeedEditText.setValidatorFunction(Calculator.Pace::parse);
        SingleInputInteractionContainer paceTospeedContainer = new SingleInputInteractionContainer.Builder(this)
                .setProperty(Property.CONVERT_PACE_TO_SPEED)
                .setKeyboard(runnersKeyboard)
                .setInput(paceToSpeedEditText)
                .setResultsTextView(findViewById(R.id.pace_to_speed_results_tv))
                .setResultsTextID(R.string.pace_to_speed_results)
                .build();
        interactionContainers.add(paceTospeedContainer);

        KeyboardlessEditText speedToPaceEditText = findViewById(R.id.speed_to_pace_et);
        speedToPaceEditText.setInput(Input.SPEED);
        speedToPaceEditText.setValidatorFunction(Calculator.Speed::parse);
        SingleInputInteractionContainer speedToPaceContainer = new SingleInputInteractionContainer.Builder(this)
                .setProperty(Property.CONVERT_SPEED_TO_PACE)
                .setKeyboard(runnersKeyboard)
                .setInput(speedToPaceEditText)
                .setResultsTextView(findViewById(R.id.speed_to_pace_results_tv))
                .setResultsTextID(R.string.speed_to_pace_results)
                .build();
        interactionContainers.add(speedToPaceContainer);

        KeyboardlessEditText calculatePaceDistanceEditText = findViewById(R.id.calculate_pace_distance_et);
        KeyboardlessEditText calculatePaceTimeEditText = findViewById(R.id.calculate_pace_time_et);
        calculatePaceDistanceEditText.setInput(Input.DISTANCE);
        calculatePaceDistanceEditText.setValidatorFunction(Calculator.Distance::parse);
        calculatePaceTimeEditText.setInput(Input.TIME);
        calculatePaceTimeEditText.setValidatorFunction(Calculator.Time::parse);
        DualInputInteractionContainer calculatePaceContainer = new DualInputInteractionContainer.Builder(this)
                .setProperty(Property.CALCULATE_PACE)
                .setKeyboard(runnersKeyboard)
                .setFirstInput(calculatePaceDistanceEditText)
                .setSecondInput(calculatePaceTimeEditText)
                .setResultsTextView(findViewById(R.id.calculate_pace_results_tv))
                .setResultsTextID(R.string.calculate_pace_results)
                .build();
        interactionContainers.add(calculatePaceContainer);

        KeyboardlessEditText calculateTimeDistanceEditText = findViewById(R.id.calculate_time_distance_et);
        KeyboardlessEditText calculateTimePaceEditText = findViewById(R.id.calculate_time_pace_et);
        calculateTimeDistanceEditText.setInput(Input.DISTANCE);
        calculateTimeDistanceEditText.setValidatorFunction(Calculator.Distance::parse);
        calculateTimePaceEditText.setInput(Input.PACE);
        calculateTimePaceEditText.setValidatorFunction(Calculator.Pace::parse);
        DualInputInteractionContainer calculateTimeContainer = new DualInputInteractionContainer.Builder(this)
                .setProperty(Property.CALCULATE_TIME)
                .setKeyboard(runnersKeyboard)
                .setFirstInput(calculateTimeDistanceEditText)
                .setSecondInput(calculateTimePaceEditText)
                .setResultsTextView(findViewById(R.id.calculate_time_results_tv))
                .setResultsTextID(R.string.calculate_time_results)
                .build();
        interactionContainers.add(calculateTimeContainer);

        KeyboardlessEditText calculateDistanceTimeEditText = findViewById(R.id.calculate_distance_time_et);
        KeyboardlessEditText calculateDistancePaceEditText = findViewById(R.id.calculate_distance_pace_et);
        calculateDistanceTimeEditText.setInput(Input.TIME);
        calculateDistanceTimeEditText.setValidatorFunction(Calculator.Time::parse);
        calculateDistancePaceEditText.setInput(Input.PACE);
        calculateDistancePaceEditText.setValidatorFunction(Calculator.Pace::parse);
        DualInputInteractionContainer calculateDistanceContainer = new DualInputInteractionContainer.Builder(this)
                .setProperty(Property.CALCULATE_DISTANCE)
                .setKeyboard(runnersKeyboard)
                .setFirstInput(calculateDistanceTimeEditText)
                .setSecondInput(calculateDistancePaceEditText)
                .setResultsTextView(findViewById(R.id.calculate_distance_results_tv))
                .setResultsTextID(R.string.calculate_distance_results)
                .build();
        interactionContainers.add(calculateDistanceContainer);

        KeyboardlessEditText calculateVO2maxEstimateCooperTestResultEditText = findViewById(R.id.calculate_vo2max_estimate_cooper_test_result_et);
        calculateVO2maxEstimateCooperTestResultEditText.setInput(Input.DISTANCE);
        calculateVO2maxEstimateCooperTestResultEditText.setValidatorFunction(Calculator.Distance::parse);
        SingleInputInteractionContainer calculateVO2MaxEstimateContainer = new SingleInputInteractionContainer.Builder(this)
                .setProperty(Property.CALCULATE_VO2MAX_ESTIMATE)
                .setKeyboard(runnersKeyboard)
                .setInput(calculateVO2maxEstimateCooperTestResultEditText)
                .setResultsTextView(findViewById(R.id.calculate_vo2max_estimate_results_tv))
                .setResultsTextID(R.string.calculate_vo2max_estimate_results)
                .build();
        interactionContainers.add(calculateVO2MaxEstimateContainer);

        TextView viewById = findViewById(R.id.calculate_training_heart_rate_zones_resting_heart_rate_et);

        calculateVO2maxEstimateCooperTestLinkImageView = findViewById(R.id.calculate_vo2max_estimate_cooper_test_link_iv);
        calculateTrainingHeartRateZonesKarvonenLinkImageView = findViewById(R.id.calculate_training_heart_rate_zones_karvonen_link_iv);
    }

    private void setGlobalTexts() {
        RunnersCalculator.pace = getString(R.string.unit_pace);
        RunnersCalculator.speed = getString(R.string.unit_speed);
        RunnersCalculator.distance = getString(R.string.unit_distance);
    }

    private void setTexts() {
        interactionContainers.forEach(InteractionContainer::setDefaultResults);

        convertPaceToSpeedTextView.setText(String.format(getString(R.string.convert_xx_to_xx), RunnersCalculator.pace, RunnersCalculator.speed));
        convertSpeedToPaceTextView.setText(String.format(getString(R.string.convert_xx_to_xx), RunnersCalculator.speed, RunnersCalculator.pace));
        calculatePaceTextView.setText(String.format(getString(R.string.calculate_xx), getString(R.string.pace).toLowerCase()));
        calculatePaceDistanceHintTextView.setText(String.format(getString(R.string.hint_distance), RunnersCalculator.distance));
        calculatePaceTimeHintTextView.setText(String.format(getString(R.string.hint_time), getString(R.string.default_time)));
        calculateTimeTextView.setText(String.format(getString(R.string.calculate_xx), getString(R.string.time).toLowerCase()));
        calculateTimeDistanceHintTextView.setText(String.format(getString(R.string.hint_distance), RunnersCalculator.distance));
        calculateTimePaceHintTextView.setText(String.format(getString(R.string.hint_pace), RunnersCalculator.pace));
        calculateDistanceTextView.setText(String.format(getString(R.string.calculate_xx), getString(R.string.distance).toLowerCase()));
        calculateDistanceTimeHintTextView.setText(String.format(getString(R.string.hint_time), getString(R.string.default_time)));
        calculateDistancePaceHintTextView.setText(String.format(getString(R.string.hint_pace), RunnersCalculator.pace));
        calculateVO2maxEstimateTextView.setText(String.format(getString(R.string.estimate_xx), getString(R.string.vo2max)));
    }

    private void setListeners() {
        interactionContainers.forEach(InteractionContainer::setListeners);

        calculateVO2maxEstimateCooperTestLinkImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_cooper_test)));
            startActivity(intent);
        });

        calculateTrainingHeartRateZonesKarvonenLinkImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_karvonen_method)));
            startActivity(intent);
        });
    }

    public static class ReferencedTextWatcher implements TextWatcher {
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
            } else { // empty input is okay, but nothing to calculate, set default result text
                setDefaultResultTextAndBackgroundResource(R.drawable.input_default);
            }
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

    public static class KeyboardHandler implements View.OnFocusChangeListener, View.OnTouchListener {
        private final RunnersKeyboard runnersKeyboard;

        public KeyboardHandler(RunnersKeyboard runnersKeyboard) {
            this.runnersKeyboard = runnersKeyboard;
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (KeyboardlessEditText.class.isAssignableFrom(view.getClass())) {
                KeyboardlessEditText keyboardlessEditText = (KeyboardlessEditText) view;
                if (hasFocus) {
                    Editable editable = keyboardlessEditText.getText();
                    InputConnection inputConnection = view.onCreateInputConnection(new EditorInfo());
                    this.runnersKeyboard.show();
                    this.runnersKeyboard.setSeparator(keyboardlessEditText.getInput().getSeparator());
                    this.runnersKeyboard.enableDeleteButton(editable != null && !editable.toString().isEmpty());
                    this.runnersKeyboard.setInputConnection(inputConnection);
                } else {
                    this.runnersKeyboard.delayedHide();
                }
            }
        }

        @Override
        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (KeyboardlessEditText.class.isAssignableFrom(view.getClass())) {
                if (runnersKeyboard.getVisibility() != View.VISIBLE) {
                    KeyboardlessEditText keyboardlessEditText = (KeyboardlessEditText) view;
                    Editable editable = keyboardlessEditText.getText();
                    InputConnection inputConnection = view.onCreateInputConnection(new EditorInfo());
                    this.runnersKeyboard.show();
                    this.runnersKeyboard.setSeparator(keyboardlessEditText.getInput().getSeparator());
                    this.runnersKeyboard.enableDeleteButton(editable != null && !editable.toString().isEmpty());
                    this.runnersKeyboard.setInputConnection(inputConnection);
                }
            }
            // let the rest of the framework handle this event also
            return false;
        }
    }
}