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
import android.widget.TextView;

import java.util.Set;
import java.util.TreeSet;

import ax.stardust.runcalc.util.Calculator;
import ax.stardust.runcalc.input.Input;
import ax.stardust.runcalc.input.Property;
import ax.stardust.runcalc.R;
import ax.stardust.runcalc.component.KeyboardlessEditText;
import ax.stardust.runcalc.component.RunnersKeyboard;

public class RunnersCalculator extends AppCompatActivity {
    private static String pace;
    private static String speed;
    private static String distance;

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

    private ImageView cooperTestLinkImageView;

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

        TextView paceToSpeedResultsTextView = findViewById(R.id.pace_to_speed_results_tv);
        KeyboardlessEditText paceToSpeedEditText = findViewById(R.id.pace_to_speed_et);
        paceToSpeedEditText.setInput(Input.PACE);
        paceToSpeedEditText.setValidatorFunction(Calculator.Pace::parse);
        interactionContainers.add(new InteractionContainer(Property.CONVERT_PACE_TO_SPEED, paceToSpeedEditText, paceToSpeedResultsTextView, R.string.pace_to_speed_results));

        TextView speedToPaceResultsTextView = findViewById(R.id.speed_to_pace_results_tv);
        KeyboardlessEditText speedToPaceEditText = findViewById(R.id.speed_to_pace_et);
        speedToPaceEditText.setInput(Input.SPEED);
        speedToPaceEditText.setValidatorFunction(Calculator.Speed::parse);
        interactionContainers.add(new InteractionContainer(Property.CONVERT_SPEED_TO_PACE, speedToPaceEditText, speedToPaceResultsTextView, R.string.speed_to_pace_results));

        TextView calculatePaceResultsTextView = findViewById(R.id.calculate_pace_results_tv);
        KeyboardlessEditText calculatePaceDistanceEditText = findViewById(R.id.calculate_pace_distance_et);
        KeyboardlessEditText calculatePaceTimeEditText = findViewById(R.id.calculate_pace_time_et);
        calculatePaceDistanceEditText.setInput(Input.DISTANCE);
        calculatePaceDistanceEditText.setValidatorFunction(Calculator.Distance::parse);
        calculatePaceTimeEditText.setInput(Input.TIME);
        calculatePaceTimeEditText.setValidatorFunction(Calculator.Time::parse);
        interactionContainers.add(new InteractionContainer(Property.CALCULATE_PACE, calculatePaceDistanceEditText, calculatePaceTimeEditText, calculatePaceResultsTextView, R.string.calculate_pace_results));

        TextView calculateTimeResultsTextView = findViewById(R.id.calculate_time_results_tv);
        KeyboardlessEditText calculateTimeDistanceEditText = findViewById(R.id.calculate_time_distance_et);
        KeyboardlessEditText calculateTimePaceEditText = findViewById(R.id.calculate_time_pace_et);
        calculateTimeDistanceEditText.setInput(Input.DISTANCE);
        calculateTimeDistanceEditText.setValidatorFunction(Calculator.Distance::parse);
        calculateTimePaceEditText.setInput(Input.PACE);
        calculateTimePaceEditText.setValidatorFunction(Calculator.Pace::parse);
        interactionContainers.add(new InteractionContainer(Property.CALCULATE_TIME, calculateTimeDistanceEditText, calculateTimePaceEditText, calculateTimeResultsTextView, R.string.calculate_time_results));

        TextView calculateDistanceResultsTextView = findViewById(R.id.calculate_distance_results_tv);
        KeyboardlessEditText calculateDistanceTimeEditText = findViewById(R.id.calculate_distance_time_et);
        KeyboardlessEditText calculateDistancePaceEditText = findViewById(R.id.calculate_distance_pace_et);
        calculateDistanceTimeEditText.setInput(Input.TIME);
        calculateDistanceTimeEditText.setValidatorFunction(Calculator.Time::parse);
        calculateDistancePaceEditText.setInput(Input.PACE);
        calculateDistancePaceEditText.setValidatorFunction(Calculator.Pace::parse);
        interactionContainers.add(new InteractionContainer(Property.CALCULATE_DISTANCE, calculateDistanceTimeEditText, calculateDistancePaceEditText, calculateDistanceResultsTextView, R.string.calculate_distance_results));

        TextView calculateVO2MaxEstimateResultsTextView = findViewById(R.id.calculate_vo2max_estimate_results_tv);
        KeyboardlessEditText calculateVO2maxEstimateCooperTestResultEditText = findViewById(R.id.calculate_vo2max_estimate_cooper_test_result_et);
        calculateVO2maxEstimateCooperTestResultEditText.setInput(Input.DISTANCE);
        calculateVO2maxEstimateCooperTestResultEditText.setValidatorFunction(Calculator.Distance::parse);
        interactionContainers.add(new InteractionContainer(Property.CALCULATE_VO2MAX_ESTIMATE, calculateVO2maxEstimateCooperTestResultEditText, calculateVO2MaxEstimateResultsTextView, R.string.calculate_vo2max_estimate_results));

        cooperTestLinkImageView = findViewById(R.id.cooper_test_link_iv);
    }

    private void setGlobalTexts() {
        RunnersCalculator.pace = getString(R.string.unit_pace);
        RunnersCalculator.speed = getString(R.string.unit_speed);
        RunnersCalculator.distance = getString(R.string.unit_distance);
    }

    private void setTexts() {
        interactionContainers.forEach(InteractionContainer::setDefaultResultText);

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

        cooperTestLinkImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_cooper_test)));
            startActivity(intent);
        });
    }

    private class InteractionContainer implements Comparable {
        private final Property property;
        private final KeyboardlessEditText firstInput;
        private final KeyboardlessEditText secondInput;
        private final TextView results;
        private final int textID; // of results text

        InteractionContainer(Property property, KeyboardlessEditText firstInput, TextView results, int textID) {
            this(property, firstInput, null, results, textID);
        }

        InteractionContainer(Property property, KeyboardlessEditText firstInput, KeyboardlessEditText secondInput, TextView results, int textID) {
            this.property = property;
            this.firstInput = firstInput;
            this.secondInput = secondInput;
            this.results = results;
            this.textID = textID;
        }

        void calculateIfPossible() {
            if (hasValidCalculationInput(firstInput)) { // criteria for all calculations
                if (secondInput == null // single input
                        || hasValidCalculationInput(secondInput)) { // or double input
                    String result = property.getCalculatorFunction().apply(getCombinedText());
                    setResultText(result);
                }
            }
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

        Property getProperty() {
            return property;
        }

        String getCombinedText() {
            String combinedText = getFirstInputText();
            String secondInputText = getSecondInputText();

            if (!secondInputText.isEmpty()) {
                combinedText += "|" + secondInputText;
            }

            return combinedText;
        }

        String getFirstInputText() {
            return getTextOfInput(firstInput);
        }

        String getSecondInputText() {
            return getTextOfInput(secondInput);
        }

        String getTextOfInput(KeyboardlessEditText input) {
            String text = "";

            if (input != null) {
                Editable editable = input.getText();
                text = editable != null ? editable.toString() : "";
            }

            return text;
        }

        void setListeners() {
            firstInput.addTextChangedListener(new ReferencedTextWatcher(this, firstInput, runnersKeyboard));
            firstInput.setOnFocusChangeListener(new KeyboardHandler(runnersKeyboard));
            firstInput.setOnTouchListener(new KeyboardHandler(runnersKeyboard));

            if (secondInput != null) {
                secondInput.addTextChangedListener(new ReferencedTextWatcher(this, secondInput, runnersKeyboard));
                secondInput.setOnFocusChangeListener(new KeyboardHandler(runnersKeyboard));
                secondInput.setOnTouchListener(new KeyboardHandler(runnersKeyboard));
            }
        }

        void setDefaultResultText() {
            switch (property) {
                case CONVERT_PACE_TO_SPEED:
                    setResultText(getString(R.string.default_speed_distance));
                    break;
                case CONVERT_SPEED_TO_PACE:
                    setResultText(getString(R.string.default_pace));
                    break;
                case CALCULATE_PACE:
                    setResultText(getString(R.string.default_pace));
                    break;
                case CALCULATE_TIME:
                    setResultText(getString(R.string.default_time));
                    break;
                case CALCULATE_DISTANCE:
                    setResultText(getString(R.string.default_speed_distance));
                case CALCULATE_VO2MAX_ESTIMATE:
                    setResultText(getString(R.string.default_vo2max));
            }
        }

        void setResultText(String result) {
            String text = getString(textID);
            text = text.replace("{pace}", RunnersCalculator.pace);
            text = text.replace("{speed}", RunnersCalculator.speed);
            text = text.replace("{distance}", RunnersCalculator.distance);
            text = text.replace("{result}", result);
            results.setText(text);
        }

        @Override
        public boolean equals(Object that) {
            if (that != null) {
                if (InteractionContainer.class.isAssignableFrom(that.getClass())) {
                    return this.property.equals(((InteractionContainer) that).getProperty());
                }
            }
            return false;
        }

        @Override
        public int compareTo(@NonNull Object that) {
            return this.property.compareTo(((InteractionContainer) that).getProperty());
        }
    }

    private class ReferencedTextWatcher implements TextWatcher {
        private final InteractionContainer interactionContainer;
        private final KeyboardlessEditText input;
        private final RunnersKeyboard runnersKeyboard;

        ReferencedTextWatcher(InteractionContainer interactionContainer, KeyboardlessEditText input, RunnersKeyboard runnersKeyboard) {
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
            interactionContainer.setDefaultResultText();
            input.setBackgroundResource(backgroundResource);
        }
    }

    private class KeyboardHandler implements View.OnFocusChangeListener, View.OnTouchListener {
        private final RunnersKeyboard runnersKeyboard;

        KeyboardHandler(RunnersKeyboard runnersKeyboard) {
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