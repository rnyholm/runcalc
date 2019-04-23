package ax.stardust.runnerscalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CalculatorActivity extends AppCompatActivity {
    private Measurement measurement = Measurement.METRIC;
    private Model model = new Model();

    private List<PropertyBoundUIComponents> propertyBoundUIComponents = new ArrayList<>();

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

        findViews();
        setTextsAccordingToMeasurement();
        setListeners();
    }

    private void findViews() {
        convertPaceToSpeedTextView = findViewById(R.id.pace_to_speed_tv);

        paceToSpeedEditText = findViewById(R.id.pace_to_speed_et);
        paceToSpeedResultsTextView = findViewById(R.id.pace_to_speed_results_tv);
        propertyBoundUIComponents.add(PropertyBoundUIComponents.create(Model.Property.PACE_TO_SPEED, paceToSpeedEditText, paceToSpeedResultsTextView));

        calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setEnabled(false);
    }

    private void setListeners() {
        propertyBoundUIComponents.forEach(o -> {
            Model.Property property = o.getProperty();
            EditText editText = o.getEditText();
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
            propertyBoundUIComponents.forEach(o -> {
                Model.Property property = o.getProperty();
                TextView textView = o.getTextView();
                if (model.isPropertyChanged(property)) {
                   property.getCalculatorFunction().
                }
            });
            propertyBoundEditText.entrySet().forEach(entry -> {
                Model.Property property = entry.getKey();
                EditText editText = entry.getValue();
                editText.setText();
            });
            Stream.of(Model.Property.values()).
            String result = Calculator.convertPaceToSpeed(paceToSpeedEditText.getText().toString());
            String pace = getStringFromResource(Measurement.METRIC.equals(measurement) ? R.string.pace_metric : R.string.pace_imperial);
            String speed = getStringFromResource(Measurement.METRIC.equals(measurement) ? R.string.speed_metric : R.string.speed_imperial);
            paceToSpeedResultsTextView.setText(String.format(getStringFromResource(R.string.pace_to_speed_results), pace, result, speed));
        });
    }

    private void setTextsAccordingToMeasurement() {
        String pace = getStringFromResource(Measurement.METRIC.equals(measurement) ? R.string.pace_metric : R.string.pace_imperial);
        String speed = getStringFromResource(Measurement.METRIC.equals(measurement) ? R.string.speed_metric : R.string.speed_imperial);
        String speedZero = getStringFromResource(R.string.speed_zero);

        convertPaceToSpeedTextView.setText(String.format(getStringFromResource(R.string.convert_xx_to_xx), pace, speed));
        paceToSpeedResultsTextView.setText(String.format(getStringFromResource(R.string.pace_to_speed_results), pace, speedZero, speed));
    }

    private String getStringFromResource(int id) {
        return getApplicationContext().getResources().getString(id);
    }

    private static class PropertyBoundUIComponents {
        private final Model.Property property;
        private final EditText editText;
        private final TextView textView;

        private PropertyBoundUIComponents(Model.Property property, EditText editText, TextView textView) {
            this.property = property;
            this.editText = editText;
            this.textView = textView;
        }

        public static PropertyBoundUIComponents create(Model.Property property, EditText editText, TextView textView) {
            return new PropertyBoundUIComponents(property, editText, textView);
        }

        public Model.Property getProperty() {
            return property;
        }

        public EditText getEditText() {
            return editText;
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
