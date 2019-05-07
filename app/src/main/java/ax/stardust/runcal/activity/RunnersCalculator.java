package ax.stardust.runcal.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;

import java.util.Set;
import java.util.TreeSet;

import ax.stardust.runcal.Calculator;
import ax.stardust.runcal.Input;
import ax.stardust.runcal.Measurement;
import ax.stardust.runcal.Property;
import ax.stardust.runcal.R;
import ax.stardust.runcal.component.KeyboardlessEditText;
import ax.stardust.runcal.component.RunnersKeyboard;

public class RunnersCalculator extends AppCompatActivity {
    private static Measurement measurement;
    private static String pace;
    private static String speed;
    private static String distance;

    private final Set<InteractionContainer> interactionContainers = new TreeSet<>();
    private RunnersKeyboard runnersKeyboard;

    // TextViews
    private TextView convertPaceToSpeedTextView;
    private TextView convertSpeedToPaceTextView;
    private TextView calculatePaceTextView;
    private TextView calculatePaceDistanceHintTextView;
    private TextView calculatePaceTimeHintTextView;
    private TextView calculatePaceResultHintTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        setMeasurement(Measurement.METRIC);
        findViews();
        setGlobalTexts();
        setTexts();
        setListeners();
    }

    @Override
    public void onBackPressed() {
        if (runnersKeyboard.getVisibility() == View.VISIBLE) {
            runnersKeyboard.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    private void setMeasurement(Measurement measurement) {
        RunnersCalculator.measurement = measurement;
    }

    private void findViews() {
        runnersKeyboard = findViewById(R.id.soft_keyboard);

        convertPaceToSpeedTextView = findViewById(R.id.pace_to_speed_tv);
        convertSpeedToPaceTextView = findViewById(R.id.speed_to_pace_tv);
        calculatePaceTextView = findViewById(R.id.calculate_pace_tv);
        calculatePaceDistanceHintTextView = findViewById(R.id.calculate_pace_distance_hint_tv);
        calculatePaceTimeHintTextView = findViewById(R.id.calculate_pace_time_hint_tv);
        calculatePaceResultHintTextView = findViewById(R.id.calculate_pace_result_hint_tv);

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
    }

    private void setGlobalTexts() {
        RunnersCalculator.pace = getString(Measurement.METRIC.equals(RunnersCalculator.measurement) ? R.string.unit_pace_metric : R.string.unit_pace_imperial);
        RunnersCalculator.speed = getString(Measurement.METRIC.equals(RunnersCalculator.measurement) ? R.string.unit_speed_metric : R.string.unit_speed_imperial);
        RunnersCalculator.distance = getString(Measurement.METRIC.equals(RunnersCalculator.measurement) ? R.string.unit_distance_metric : R.string.unit_distance_imperial);
    }

    private void setTexts() {
        interactionContainers.forEach(InteractionContainer::setDefaultResultText);

        convertPaceToSpeedTextView.setText(String.format(getString(R.string.convert_xx_to_xx), RunnersCalculator.pace, RunnersCalculator.speed));
        convertSpeedToPaceTextView.setText(String.format(getString(R.string.convert_xx_to_xx), RunnersCalculator.speed, RunnersCalculator.pace));
        calculatePaceTextView.setText(String.format(getString(R.string.calculate_xx), getString(R.string.pace)));
        calculatePaceDistanceHintTextView.setText(String.format(getString(R.string.hint_distance), RunnersCalculator.distance));
        calculatePaceTimeHintTextView.setText(String.format(getString(R.string.hint_time), getString(R.string.default_time)));
        calculatePaceResultHintTextView.setText(String.format(getString(R.string.hint_pace), RunnersCalculator.pace));
    }

    private void setListeners() {
        interactionContainers.forEach(InteractionContainer::setListeners);
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
            if (!getFirstInputText().isEmpty()) { // criteria for all calculations
                if (secondInput == null // single input
                        || !getSecondInputText().isEmpty()) { // or double input
                    String result = property.getCalculatorFunction().apply(getCombinedText());
                    setResultText(result);
                }
            }
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
            firstInput.addTextChangedListener(new ReferencedTextWatcher(this, firstInput));
            firstInput.setOnFocusChangeListener(new KeyboardHandler(runnersKeyboard));

            if (secondInput != null) {
                secondInput.addTextChangedListener(new ReferencedTextWatcher(this, secondInput));
                secondInput.setOnFocusChangeListener(new KeyboardHandler(runnersKeyboard));
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

        ReferencedTextWatcher(InteractionContainer interactionContainer, KeyboardlessEditText input) {
            this.interactionContainer = interactionContainer;
            this.input = input;
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

    private class KeyboardHandler implements View.OnFocusChangeListener {
        private final RunnersKeyboard runnersKeyboard;

        KeyboardHandler(RunnersKeyboard runnersKeyboard) {
            this.runnersKeyboard = runnersKeyboard;
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (KeyboardlessEditText.class.isAssignableFrom(view.getClass())) {
                KeyboardlessEditText keyboardlessEditText = (KeyboardlessEditText) view;
                if (hasFocus) {
                    InputConnection ic = view.onCreateInputConnection(new EditorInfo());
                    this.runnersKeyboard.setSeparator(keyboardlessEditText.getInput().getSeparator());
                    this.runnersKeyboard.setInputConnection(ic);
                    this.runnersKeyboard.setVisibility(View.VISIBLE);
                } else {
                    this.runnersKeyboard.setVisibility(View.GONE);
                }
            }
        }
    }
}