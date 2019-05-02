package ax.stardust.runcal.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ax.stardust.runcal.Measurement;
import ax.stardust.runcal.Model;
import ax.stardust.runcal.R;
import ax.stardust.runcal.component.KeyboardlessEditText;
import ax.stardust.runcal.component.RunnersKeyboard;

import static ax.stardust.runcal.Model.Property.CALCULATE_PACE;
import static ax.stardust.runcal.Model.Property.CONVERT_PACE_TO_SPEED;
import static ax.stardust.runcal.Model.Property.CONVERT_SPEED_TO_PACE;

public class RunnersCalculator extends AppCompatActivity {
    private static Measurement measurement;
    private static String pace;
    private static String speed;
    private static String distance;

    private final List<PropertyBoundUIComponents> propertyBoundUIComponents = new ArrayList<>();
    private final Model model = new Model();

    private RunnersKeyboard runnersKeyboard;

    // TextViews
    private TextView convertPaceToSpeedTextView;
    private TextView paceToSpeedResultsTextView;
    private TextView convertSpeedToPaceTextView;
    private TextView speedToPaceResultsTextView;
    private TextView calculatePaceTextView;
    private TextView calculatePaceDistanceHintTextView;
    private TextView calculatePaceTimeHintTextView;
    private TextView calculatePaceResultHintTextView;
    private TextView calculatePaceResultsTextView;

    // EditTexts
    private KeyboardlessEditText paceToSpeedEditText;
    private KeyboardlessEditText speedToPaceEditText;
    private KeyboardlessEditText calculatePaceDistanceEditText;

    // Button
    private Button calculateButton;

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

    private void setMeasurement(Measurement measurement) {
        RunnersCalculator.measurement = measurement;
    }

    private void findViews() {
        runnersKeyboard = findViewById(R.id.soft_keyboard);

        convertPaceToSpeedTextView = findViewById(R.id.pace_to_speed_tv);
        paceToSpeedEditText = findViewById(R.id.pace_to_speed_et);
        paceToSpeedResultsTextView = findViewById(R.id.pace_to_speed_results_tv);
        propertyBoundUIComponents.add(new PropertyBoundUIComponents(CONVERT_PACE_TO_SPEED, new InputBoundEditText(paceToSpeedEditText, Model.Input.PACE), paceToSpeedResultsTextView, R.string.pace_to_speed_results));

        convertSpeedToPaceTextView = findViewById(R.id.speed_to_pace_tv);
        speedToPaceEditText = findViewById(R.id.speed_to_pace_et);
        speedToPaceResultsTextView = findViewById(R.id.speed_to_pace_results_tv);
        propertyBoundUIComponents.add(new PropertyBoundUIComponents(CONVERT_SPEED_TO_PACE, new InputBoundEditText(speedToPaceEditText, Model.Input.SPEED), speedToPaceResultsTextView, R.string.speed_to_pace_results));

        calculatePaceTextView = findViewById(R.id.calculate_pace_tv);
        calculatePaceDistanceHintTextView = findViewById(R.id.calculate_pace_distance_hint_tv);
        calculatePaceTimeHintTextView = findViewById(R.id.calculate_pace_time_hint_tv);
        calculatePaceResultHintTextView = findViewById(R.id.calculate_pace_result_hint_tv);
        calculatePaceDistanceEditText = findViewById(R.id.calculate_pace_distance_et);
        KeyboardlessEditText calculatePaceTimeEditText = findViewById(R.id.calculate_pace_time_et);
        calculatePaceResultsTextView = findViewById(R.id.calculate_pace_results_tv);
        propertyBoundUIComponents.add(new PropertyBoundUIComponents(CALCULATE_PACE, new InputBoundEditText(calculatePaceDistanceEditText, Model.Input.DISTANCE), new InputBoundEditText(calculatePaceTimeEditText, Model.Input.DISTANCE), calculatePaceResultsTextView, R.string.calculate_pace_results));

        calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setEnabled(false);
    }

    private void setGlobalTexts() {
        RunnersCalculator.pace = getString(Measurement.METRIC.equals(RunnersCalculator.measurement) ? R.string.unit_pace_metric : R.string.unit_pace_imperial);
        RunnersCalculator.speed = getString(Measurement.METRIC.equals(RunnersCalculator.measurement) ? R.string.unit_speed_metric : R.string.unit_speed_imperial);
        RunnersCalculator.distance = getString(Measurement.METRIC.equals(RunnersCalculator.measurement) ? R.string.unit_distance_metric : R.string.unit_distance_imperial);
    }

    private void setTexts() {
        propertyBoundUIComponents.forEach(PropertyBoundUIComponents::setDefaultResultText);

        convertPaceToSpeedTextView.setText(String.format(getString(R.string.convert_xx_to_xx), RunnersCalculator.pace, RunnersCalculator.speed));
        convertSpeedToPaceTextView.setText(String.format(getString(R.string.convert_xx_to_xx), RunnersCalculator.speed, RunnersCalculator.pace));
        calculatePaceTextView.setText(String.format(getString(R.string.calculate_xx), getString(R.string.pace)));
        calculatePaceDistanceHintTextView.setText(String.format(getString(R.string.hint_distance), RunnersCalculator.distance));
        calculatePaceTimeHintTextView.setText(String.format(getString(R.string.hint_time), getString(R.string.default_time)));
        calculatePaceResultHintTextView.setText(String.format(getString(R.string.hint_pace), RunnersCalculator.pace));
    }

    private void setListeners() {
        propertyBoundUIComponents.forEach(boundUIComponents -> {
            Model.Property property = boundUIComponents.getProperty();
            InputBoundEditText firstInputBoundEditText = boundUIComponents.getFirstInputBoundEditText();
            EditText firstEditText = firstInputBoundEditText.getEditText();
            firstEditText.addTextChangedListener(new PropertyBoundTextWatcher(property, Model.ValueSelection.FIRST));
            firstEditText.setOnFocusChangeListener(new BoundRunnersInputFocusWatcher(firstInputBoundEditText.getInput(), runnersKeyboard));

            if (property.isPairedInput()) {
                InputBoundEditText secondInputBoundEditText = boundUIComponents.getSecondInputBoundEditText();
                EditText secondEditText = secondInputBoundEditText.getSecondEditText();
                secondEditText.addTextChangedListener(new PropertyBoundTextWatcher(property, Model.ValueSelection.SECOND));
                secondEditText.setOnFocusChangeListener(new BoundRunnersInputFocusWatcher(secondInputBoundEditText.getInput()runnersKeyboard));
            }
        });

        calculateButton.setOnClickListener(view -> {
            propertyBoundUIComponents.forEach(boundUIComponents -> {
                Model.Property property = boundUIComponents.getProperty();
                if (model.isPropertyChanged(property)) {
                    try {
                        String result = property.getCalculatorFunction().apply(model.getPropertyValue(property));
                        boundUIComponents.setResultText(result);
                        model.commitProperty(property);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.invalid_input_error, Toast.LENGTH_LONG).show();
                        Log.e(RunnersCalculator.class.getSimpleName(), "Calculation error", e);
                    }
                }
            });
            calculateButton.setEnabled(model.changed());
        });
    }

    private class PropertyBoundUIComponents {
        private final Model.Property property;

        private final InputBoundEditText firstInputBoundEditText;
        private final InputBoundEditText secondInputBoundEditText;

        private final TextView textView;
        private final int textID;

        PropertyBoundUIComponents(Model.Property property, InputBoundEditText firstInputBoundEditText, InputBoundEditText secondInputBoundEditText, TextView textView, int textID) {
            if (property.isPairedInput() && secondInputBoundEditText == null) {
                throw new IllegalArgumentException(PropertyBoundUIComponents.class.getSimpleName() + " cannot be created with secondInputBoundEditText set to null when property expecting paired input is bound to this object");
            }
            this.property = property;
            this.firstInputBoundEditText = firstInputBoundEditText;
            this.secondInputBoundEditText = secondInputBoundEditText;
            this.textView = textView;
            this.textID = textID;
        }

        PropertyBoundUIComponents(Model.Property property, InputBoundEditText firstInputBoundEditText, TextView textView, int textID) {
            this(property, firstInputBoundEditText, null, textView, textID);
        }

        Model.Property getProperty() {
            return property;
        }

        public InputBoundEditText getFirstInputBoundEditText() {
            return firstInputBoundEditText;
        }

        public InputBoundEditText getSecondInputBoundEditText() {
            return secondInputBoundEditText;
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
            textView.setText(text);
        }
    }

    private class InputBoundEditText {
        private final EditText editText;
        private final Model.Input input;

        public InputBoundEditText(EditText editText, Model.Input input) {
            if (editText == null || !Model.Input.NONE.equals(input)) {
                throw new IllegalArgumentException(InputBoundEditText.class.getSimpleName() + " cannot be created with editText set to null or input NONE");
            }
            this.editText = editText;
            this.input = input;
        }

        public EditText getEditText() {
            return editText;
        }

        public Model.Input getInput() {
            return input;
        }
    }

    private class PropertyBoundTextWatcher implements TextWatcher {
        private final Model.Property property;
        private final Model.ValueSelection selection;

        PropertyBoundTextWatcher(Model.Property property, Model.ValueSelection selection) {
            this.property = property;
            this.selection = selection;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Do nothing...
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            model.setPropertyValue(property, selection, charSequence.toString());
            calculateButton.setEnabled(model.changed());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Do nothing...
        }
    }

    private class BoundRunnersInputFocusWatcher implements View.OnFocusChangeListener {

        private final Model.Input input;
        private final RunnersKeyboard runnersKeyboard;

        public BoundRunnersInputFocusWatcher(Model.Input input, RunnersKeyboard runnersKeyboard) {
            this.input = input;
            this.runnersKeyboard = runnersKeyboard;
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (KeyboardlessEditText.class.isAssignableFrom(view.getClass())) {
                if (hasFocus) {
                    Log.d(RunnersCalculator.class.getSimpleName(), view.getId() + " FOCUS GAINED");
                    InputConnection ic = view.onCreateInputConnection(new EditorInfo());
                    this.runnersKeyboard.setSeparatorCharacter(input.getSeparator());
                    this.runnersKeyboard.setInputConnection(ic);

                    this.runnersKeyboard.setVisibility(View.VISIBLE);
                } else {
                    Log.d(RunnersCalculator.class.getSimpleName(), view.getId() + " FOCUS LOST");
                    this.runnersKeyboard.setVisibility(View.GONE);
                }
            }
        }
    }
}
