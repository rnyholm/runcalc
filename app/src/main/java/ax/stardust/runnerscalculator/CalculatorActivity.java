package ax.stardust.runnerscalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static ax.stardust.runnerscalculator.Model.Property.*;

public class CalculatorActivity extends AppCompatActivity {
    private static Measurement measurement;
    private static String pace;
    private static String speed;
    private static String distance;

    private List<PropertyBoundUIComponents> propertyBoundUIComponents = new ArrayList<>();
    private Model model = new Model();

    // TextViews
    private TextView convertPaceToSpeedTextView;
    private TextView paceToSpeedResultsTextView;

    // EditTexts
    private EditText paceToSpeedEditText;

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
        CalculatorActivity.measurement = measurement;
    }

    private void findViews() {
        convertPaceToSpeedTextView = findViewById(R.id.pace_to_speed_tv);

        paceToSpeedEditText = findViewById(R.id.pace_to_speed_et);
        paceToSpeedResultsTextView = findViewById(R.id.pace_to_speed_results_tv);
        propertyBoundUIComponents.add(new PropertyBoundUIComponents(CONVERT_PACE_TO_SPEED, paceToSpeedEditText, paceToSpeedResultsTextView, R.string.pace_to_speed_results));

        calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setEnabled(false);
    }

    private void setGlobalTexts() {
        CalculatorActivity.pace = getString(Measurement.METRIC.equals(CalculatorActivity.measurement) ? R.string.unit_pace_metric : R.string.unit_pace_imperial);
        CalculatorActivity.speed = getString(Measurement.METRIC.equals(CalculatorActivity.measurement) ? R.string.unit_speed_metric : R.string.unit_speed_imperial);
        CalculatorActivity.distance = getString(Measurement.METRIC.equals(CalculatorActivity.measurement) ? R.string.unit_distance_metric : R.string.unit_distance_imperial);
    }

    private void setTexts() {
        propertyBoundUIComponents.forEach(boundUIComponents -> {
            boundUIComponents.setDefaultResultText();
        });

        convertPaceToSpeedTextView.setText(String.format(getString(R.string.convert_xx_to_xx), pace, speed));
    }

    private void setListeners() {
        propertyBoundUIComponents.forEach(boundUIComponents -> {
            Model.Property property = boundUIComponents.getProperty();
            EditText editText = boundUIComponents.getEditText();
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Do nothing...
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    model.setPropertyValue(property, charSequence.toString());
                    calculateButton.setEnabled(model.changed());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Do nothing...
                }
            });
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
                    }
                }
            });

            calculateButton.setEnabled(model.changed());
        });
    }

    private class PropertyBoundUIComponents {
        private final Model.Property property;
        private final EditText editText;
        private final TextView textView;
        private final int textID;

        PropertyBoundUIComponents(Model.Property property, EditText editText, TextView textView, int textID) {
            this.property = property;
            this.editText = editText;
            this.textView = textView;
            this.textID = textID;
        }

//        PropertyBoundUIComponents create(Model.Property property, EditText editText, TextView textView, int textID) {
//            return new PropertyBoundUIComponents(property, editText, textView, textID);
//        }

        Model.Property getProperty() {
            return property;
        }

        EditText getEditText() {
            return editText;
        }

        void setDefaultResultText() {
            switch (property) {
                case CONVERT_PACE_TO_SPEED:
                    setResultText(getString(R.string.default_speed));
                case CONVERT_SPEED_TO_PACE:
                    setResultText(getString(R.string.default_pace));
                case CALCULATE_PACE:
                    setResultText(getString(R.string.default_pace));
                case CALCULATE_TIME:
                    setResultText(getString(R.string.default_time));
                case CALCULATE_DISTANCE:
                    setResultText(getString(R.string.default_speed));
            }
        }

        void setResultText(String result) {
            String text = getString(textID);
            text = text.replace("{pace}", CalculatorActivity.pace);
            text = text.replace("{speed}", CalculatorActivity.speed);
            text = text.replace("{distance}", CalculatorActivity.distance);
            text = text.replace("{result}", result);
            textView.setText(text);
        }
    }
}
