package ax.stardust.runcalc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Set;
import java.util.TreeSet;

import ax.stardust.runcalc.R;
import ax.stardust.runcalc.component.keyboard.RunnersKeyboard;
import ax.stardust.runcalc.component.widget.KeyboardlessEditText;
import ax.stardust.runcalc.function.Calculator;
import ax.stardust.runcalc.function.Property;
import ax.stardust.runcalc.interaction.Input;
import ax.stardust.runcalc.interaction.container.DualInputInteractionContainer;
import ax.stardust.runcalc.interaction.container.FinishTimePredictionsInteractionContainer;
import ax.stardust.runcalc.interaction.container.HeartRateZonesInteractionContainer;
import ax.stardust.runcalc.interaction.container.InteractionContainer;
import ax.stardust.runcalc.interaction.container.SingleInputInteractionContainer;

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
    private TextView vo2maxEstimateTextView;
    private TextView finishTimePredictionsTextView;
    private TextView finishTimePredictions100mHeadingTextView;
    private TextView finishTimePredictions200mHeadingTextView;
    private TextView finishTimePredictions400mHeadingTextView;
    private TextView finishTimePredictions800mHeadingTextView;
    private TextView finishTimePredictions1500mHeadingTextView;
    private TextView finishTimePredictions1miHeadingTextView;
    private TextView finishTimePredictions2miHeadingTextView;
    private TextView finishTimePredictions5kmHeadingTextView;
    private TextView finishTimePredictions5miHeadingTextView;
    private TextView finishTimePredictions10kmHeadingTextView;
    private TextView finishTimePredictions10miHeadingTextView;
    private TextView finishTimePredictions50kmHeadingTextView;
    private TextView finishTimePredictions50miHeadingTextView;
    private TextView versionNameTextView;

    private RadioButton finishTimePredictionsDistance5kmRadioButton;
    private RadioButton finishTimePredictionsDistance10kmRadioButton;

    private ImageView vo2maxEstimateCooperTestLinkImageView;
    private ImageView heartRateZonesKarvonenLinkImageView;
    private ImageView finishTimePredictionsPeterRiegelLinkImageView;

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
        vo2maxEstimateTextView = findViewById(R.id.vo2max_estimate_tv);
        finishTimePredictionsTextView = findViewById(R.id.finish_time_predictions_tv);
        finishTimePredictions100mHeadingTextView = findViewById(R.id.finish_time_predictions_100m_heading_tv);
        finishTimePredictions200mHeadingTextView = findViewById(R.id.finish_time_predictions_200m_heading_tv);
        finishTimePredictions400mHeadingTextView = findViewById(R.id.finish_time_predictions_400m_heading_tv);
        finishTimePredictions800mHeadingTextView = findViewById(R.id.finish_time_predictions_800m_heading_tv);
        finishTimePredictions1500mHeadingTextView = findViewById(R.id.finish_time_predictions_1500m_heading_tv);
        finishTimePredictions1miHeadingTextView = findViewById(R.id.finish_time_predictions_1mi_heading_tv);
        finishTimePredictions2miHeadingTextView = findViewById(R.id.finish_time_predictions_2mi_heading_tv);
        finishTimePredictions5kmHeadingTextView = findViewById(R.id.finish_time_predictions_5km_heading_tv);
        finishTimePredictions5miHeadingTextView = findViewById(R.id.finish_time_predictions_5mi_heading_tv);
        finishTimePredictions10kmHeadingTextView = findViewById(R.id.finish_time_predictions_10km_heading_tv);
        finishTimePredictions10miHeadingTextView = findViewById(R.id.finish_time_predictions_10mi_heading_tv);
        finishTimePredictions50kmHeadingTextView = findViewById(R.id.finish_time_predictions_50km_heading_tv);
        finishTimePredictions50miHeadingTextView = findViewById(R.id.finish_time_predictions_50mi_heading_tv);
        versionNameTextView = findViewById(R.id.version_name_tv);

        KeyboardlessEditText paceToSpeedEditText = findViewById(R.id.pace_to_speed_et);
        paceToSpeedEditText.setInput(Input.PACE);
        paceToSpeedEditText.setValidatorFunction(Calculator.Pace::parse);
        SingleInputInteractionContainer paceToSpeedContainer = new SingleInputInteractionContainer.Builder(this)
                .setProperty(Property.CONVERT_PACE_TO_SPEED)
                .setKeyboard(runnersKeyboard)
                .setInput(paceToSpeedEditText)
                .setResultsTextView(findViewById(R.id.pace_to_speed_results_tv))
                .setResultsTextID(R.string.pace_to_speed_results)
                .build();
        interactionContainers.add(paceToSpeedContainer);

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
        calculatePaceDistanceEditText.setInput(Input.DISTANCE_KM);
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
        calculateTimeDistanceEditText.setInput(Input.DISTANCE_KM);
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

        KeyboardlessEditText vo2maxEstimateCooperTestResultEditText = findViewById(R.id.vo2max_estimate_cooper_test_result_et);
        vo2maxEstimateCooperTestResultEditText.setInput(Input.DISTANCE_M);
        vo2maxEstimateCooperTestResultEditText.setValidatorFunction(Calculator.Distance::parseMetersStrict);
        SingleInputInteractionContainer vo2MaxEstimateContainer = new SingleInputInteractionContainer.Builder(this)
                .setProperty(Property.CALCULATE_VO2MAX_ESTIMATE)
                .setKeyboard(runnersKeyboard)
                .setInput(vo2maxEstimateCooperTestResultEditText)
                .setResultsTextView(findViewById(R.id.vo2max_estimate_results_tv))
                .setResultsTextID(R.string.vo2max_estimate_results)
                .build();
        interactionContainers.add(vo2MaxEstimateContainer);

        KeyboardlessEditText maximumHeartRateEditText = findViewById(R.id.heart_rate_zones_maximum_heart_rate_et);
        maximumHeartRateEditText.setInput(Input.HEART_RATE);
        maximumHeartRateEditText.setValidatorFunction(Calculator.MaximumHeartRate::parse);
        KeyboardlessEditText restingHeartRateEditText = findViewById(R.id.heart_rate_zones_resting_heart_rate_et);
        restingHeartRateEditText.setInput(Input.HEART_RATE);
        restingHeartRateEditText.setValidatorFunction(Calculator.RestingHeartRate::parse);
        KeyboardlessEditText ageEditText = findViewById(R.id.heart_rate_zones_age_et);
        ageEditText.setInput(Input.AGE);
        ageEditText.setValidatorFunction(Calculator.Age::parse);
        SwitchCompat trainingExperienceSwitch = findViewById(R.id.heart_rate_zones_training_experience_sw);
        HeartRateZonesInteractionContainer heartRateZonesContainer = new HeartRateZonesInteractionContainer.Builder(this)
                .setProperty(Property.CALCULATE_HEART_RATE_ZONES)
                .setKeyboard(runnersKeyboard)
                .setMaximumHeartRateInput(maximumHeartRateEditText)
                .setRestingHeartRateInput(restingHeartRateEditText)
                .setAgeInput(ageEditText)
                .setExperiencedRunnerInput(trainingExperienceSwitch)
                .setHeartRateZone1ResultsTextView(findViewById(R.id.heart_rate_zones_zone1_results_tv))
                .setHeartRateZone2ResultsTextView(findViewById(R.id.heart_rate_zones_zone2_results_tv))
                .setHeartRateZone3ResultsTextView(findViewById(R.id.heart_rate_zones_zone3_results_tv))
                .setHeartRateZone4ResultsTextView(findViewById(R.id.heart_rate_zones_zone4_results_tv))
                .setHeartRateZone5ResultsTextView(findViewById(R.id.heart_rate_zones_zone5_results_tv))
                .build();
        interactionContainers.add(heartRateZonesContainer);

        finishTimePredictionsDistance5kmRadioButton = findViewById(R.id.finish_time_predictions_distance_5km_rb);
        finishTimePredictionsDistance10kmRadioButton = findViewById(R.id.finish_time_predictions_distance_10km_rb);
        KeyboardlessEditText finishTimeEditText = findViewById(R.id.finish_time_predictions_finish_time_et);
        finishTimeEditText.setInput(Input.TIME);
        finishTimeEditText.setValidatorFunction(Calculator.Time::parse);
        RadioGroup distanceRadioGroup = findViewById(R.id.finish_time_predictions_distance_rg);
        FinishTimePredictionsInteractionContainer finishTimePredictionsContainer = new FinishTimePredictionsInteractionContainer.Builder(this)
                .setProperty(Property.CALCULATE_FINISH_TIME_PREDICTIONS)
                .setKeyboard(runnersKeyboard)
                .setFinishTimeInput(finishTimeEditText)
                .setDistanceInputGroup(distanceRadioGroup)
                .setDistance5kmInput(finishTimePredictionsDistance5kmRadioButton)
                .setResults100mTimeTextView(findViewById(R.id.finish_time_predictions_100m_time_tv))
                .setResults100mPaceTextView(findViewById(R.id.finish_time_predictions_100m_pace_tv))
                .setResults200mTimeTextView(findViewById(R.id.finish_time_predictions_200m_time_tv))
                .setResults200mPaceTextView(findViewById(R.id.finish_time_predictions_200m_pace_tv))
                .setResults400mTimeTextView(findViewById(R.id.finish_time_predictions_400m_time_tv))
                .setResults400mPaceTextView(findViewById(R.id.finish_time_predictions_400m_pace_tv))
                .setResults800mTimeTextView(findViewById(R.id.finish_time_predictions_800m_time_tv))
                .setResults800mPaceTextView(findViewById(R.id.finish_time_predictions_800m_pace_tv))
                .setResults1500mTimeTextView(findViewById(R.id.finish_time_predictions_1500m_time_tv))
                .setResults1500mPaceTextView(findViewById(R.id.finish_time_predictions_1500m_pace_tv))
                .setResults1miTimeTextView(findViewById(R.id.finish_time_predictions_1mi_time_tv))
                .setResults1miPaceTextView(findViewById(R.id.finish_time_predictions_1mi_pace_tv))
                .setResults2miTimeTextView(findViewById(R.id.finish_time_predictions_2mi_time_tv))
                .setResults2miPaceTextView(findViewById(R.id.finish_time_predictions_2mi_pace_tv))
                .setResults5kmTimeTextView(findViewById(R.id.finish_time_predictions_5km_time_tv))
                .setResults5kmPaceTextView(findViewById(R.id.finish_time_predictions_5km_pace_tv))
                .setResults5miTimeTextView(findViewById(R.id.finish_time_predictions_5mi_time_tv))
                .setResults5miPaceTextView(findViewById(R.id.finish_time_predictions_5mi_pace_tv))
                .setResults10kmTimeTextView(findViewById(R.id.finish_time_predictions_10km_time_tv))
                .setResults10kmPaceTextView(findViewById(R.id.finish_time_predictions_10km_pace_tv))
                .setResults10miTimeTextView(findViewById(R.id.finish_time_predictions_10mi_time_tv))
                .setResults10miPaceTextView(findViewById(R.id.finish_time_predictions_10mi_pace_tv))
                .setResultsHalfMarathonTimeTextView(findViewById(R.id.finish_time_predictions_half_marathon_time_tv))
                .setResultsHalfMarathonPaceTextView(findViewById(R.id.finish_time_predictions_half_marathon_pace_tv))
                .setResultsMarathonTimeTextView(findViewById(R.id.finish_time_predictions_marathon_time_tv))
                .setResultsMarathonPaceTextView(findViewById(R.id.finish_time_predictions_marathon_pace_tv))
                .setResults50kmTimeTextView(findViewById(R.id.finish_time_predictions_50km_time_tv))
                .setResults50kmPaceTextView(findViewById(R.id.finish_time_predictions_50km_pace_tv))
                .setResults50miTimeTextView(findViewById(R.id.finish_time_predictions_50mi_time_tv))
                .setResults50miPaceTextView(findViewById(R.id.finish_time_predictions_50mi_pace_tv))
                .build();
        interactionContainers.add(finishTimePredictionsContainer);

        vo2maxEstimateCooperTestLinkImageView = findViewById(R.id.vo2max_estimate_cooper_test_link_iv);
        heartRateZonesKarvonenLinkImageView = findViewById(R.id.heart_rate_zones_karvonen_link_iv);
        finishTimePredictionsPeterRiegelLinkImageView = findViewById(R.id.finish_time_predictions_peter_riegel_link_iv);
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
        vo2maxEstimateTextView.setText(String.format(getString(R.string.estimate_xx), getString(R.string.vo2max)));
        finishTimePredictionsTextView.setText(String.format(getString(R.string.estimate_xx), getString(R.string.finish_times).toLowerCase()));
        finishTimePredictionsDistance5kmRadioButton.setText(getString(R.string.distance_5_unit).replace("{unit}", RunnersCalculator.distance));
        finishTimePredictionsDistance10kmRadioButton.setText(getString(R.string.distance_10_unit).replace("{unit}", RunnersCalculator.distance));
        finishTimePredictions100mHeadingTextView.setText(getString(R.string.distance_100_unit).replace("{unit}", getString(R.string.unit_meters)));
        finishTimePredictions200mHeadingTextView.setText(getString(R.string.distance_200_unit).replace("{unit}", getString(R.string.unit_meters)));
        finishTimePredictions400mHeadingTextView.setText(getString(R.string.distance_400_unit).replace("{unit}", getString(R.string.unit_meters)));
        finishTimePredictions800mHeadingTextView.setText(getString(R.string.distance_800_unit).replace("{unit}", getString(R.string.unit_meters)));
        finishTimePredictions1500mHeadingTextView.setText(getString(R.string.distance_1500_unit).replace("{unit}", getString(R.string.unit_meters)));
        finishTimePredictions1miHeadingTextView.setText(getString(R.string.distance_1_unit).replace("{unit}", getString(R.string.unit_mile)));
        finishTimePredictions2miHeadingTextView.setText(getString(R.string.distance_2_unit).replace("{unit}", getString(R.string.unit_miles)));
        finishTimePredictions5kmHeadingTextView.setText(getString(R.string.distance_5_unit).replace("{unit}", getString(R.string.unit_kilometres)));
        finishTimePredictions5miHeadingTextView.setText(getString(R.string.distance_5_unit).replace("{unit}", getString(R.string.unit_miles)));
        finishTimePredictions10kmHeadingTextView.setText(getString(R.string.distance_10_unit).replace("{unit}", getString(R.string.unit_kilometres)));
        finishTimePredictions10miHeadingTextView.setText(getString(R.string.distance_10_unit).replace("{unit}", getString(R.string.unit_miles)));
        finishTimePredictions50kmHeadingTextView.setText(getString(R.string.distance_50_unit).replace("{unit}", getString(R.string.unit_kilometres)));
        finishTimePredictions50miHeadingTextView.setText(getString(R.string.distance_50_unit).replace("{unit}", getString(R.string.unit_miles)));
    }

    private void setListeners() {
        interactionContainers.forEach(InteractionContainer::setListeners);

        vo2maxEstimateCooperTestLinkImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_cooper_test)));
            startActivity(intent);
        });

        heartRateZonesKarvonenLinkImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_karvonen_method)));
            startActivity(intent);
        });

        finishTimePredictionsPeterRiegelLinkImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_peter_riegel_method)));
            startActivity(intent);
        });
    }
}