package ax.stardust.runcalc.function;

import com.google.gson.GsonBuilder;

import java.util.Locale;

import ax.stardust.runcalc.pojo.HeartRateZones;

import static ax.stardust.runcalc.pojo.HeartRateZones.*;

public class Calculator {
    private static final int SECONDS_IN_HOUR = 3600;

    private static final double SECOND_IN_MINUTE = 0.0166666667;

    private static final String PACE_PATTERN = "^[0-9]*$|^[0-9]*:[0-9]*$";
    private static final String SPEED_DISTANCE_PATTERN = "^[0-9]*$|^[0-9]*\\.[0-9]*$";
    private static final String TIME_PATTERN = "^[0-9]{1,2}:[0-9]{1,2}$|^[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}$";
    private static final String SIMPLE_COMBINED_STRING_PATTERN = "^[^|]+\\|[^|]+$";
    private static final String TRAINING_EXPERIENCE_HRREST_HRMAX_PATTERN = "^[B|E]\\|[0-9]{2,3}\\|HR-[0-9]{3}$";
    private static final String TRAINING_EXPERIENCE_HRREST_AGE_PATTERN = "^[B|E]\\|[0-9]{2,3}\\|A-[0-9]{2}$";

    public static String convertPaceToSpeed(String pace) {
        return Pace.parse(pace).asSpeed();
    }

    public static String convertSpeedToPace(String speed) {
        return Speed.parse(speed).asPace();
    }

    /**
     * To calculate pace from distance and time, which are <br />
     * combined in the string parameter in following pattern: <br />
     * 5.6|00:23:46
     *
     * @param distanceAndTime Combined value of distance and time
     * @return Calculated pace
     */
    public static String calculatePace(String distanceAndTime) {
        throwExceptionIfMalformedStringPattern(distanceAndTime);
        String[] split = distanceAndTime.split("\\|");

        Distance distance = Distance.parse(split[0]);
        Time time = Time.parse(split[1]);

        int paceInSeconds = 0;
        if (distance.hasValue() && time.hasValue()) {
            paceInSeconds = (int) Math.round(time.inSeconds() / distance.getDistance());
        }

        return Pace.parseSeconds(paceInSeconds);
    }

    /**
     * To calculate time from distance and pace, which are <br />
     * combined in the string parameter in following pattern: <br />
     * 5.6|5:10
     *
     * @param distanceAndPace Combined value of distance and pace
     * @return Calculated time
     */
    public static String calculateTime(String distanceAndPace) {
        throwExceptionIfMalformedStringPattern(distanceAndPace);
        String[] split = distanceAndPace.split("\\|");

        Distance distance = Distance.parse(split[0]);
        Pace pace = Pace.parse(split[1]);

        int timeInSeconds = 0;
        if (distance.hasValue() && pace.hasValue()) {
            timeInSeconds = (int) Math.round(distance.getDistance() * pace.inSeconds());
        }

        return Time.parseSeconds(timeInSeconds);
    }

    /**
     * To calculate distance from time and pace, which are <br />
     * combined in the string parameter in following pattern: <br />
     * 00:23:14|5:10
     *
     * @param timeAndPace Combined value of time and pace
     * @return Calculated distance
     */
    public static String calculateDistance(String timeAndPace) {
        throwExceptionIfMalformedStringPattern(timeAndPace);
        String[] split = timeAndPace.split("\\|");

        Time time = Time.parse(split[0]);
        Pace pace = Pace.parse(split[1]);

        double distance = 0;
        if (time.hasValue() && pace.hasValue()) {
            distance = ((double) time.inSeconds()) / pace.inSeconds();
        }

        return String.format(Locale.ENGLISH, "%.2f", distance);
    }

    /**
     * To calculate an estimated vo2max from given cooper test result in meters.<br />
     * The given result must be between 550 and 5000 meters as any other result would give <br />
     * an unrealistic vo2max estimate.
     *
     * @param cooperTestResult Result from cooper test in meters
     * @return Estimated vo2max
     */
    public static String calculateVO2MaxEstimate(String cooperTestResult) {
        double result = Distance.parse(cooperTestResult).getDistance();
        if (result < 550 || result > 5000) {
            throw new IllegalArgumentException(); // unrealistic vo2max results
        }

        double VO2Max = (result - 504.9) / 44.73;
        return String.format(Locale.ENGLISH, "%.1f", VO2Max);
    }

    /**
     * To calculate the different heart rate zones for training based on given data. The data can be provided in
     * two ways. Either by providing training experience, rest heart rate and maximum heart rate. This
     * way is preferred as it relies on an actual maximum heart rate. Pattern for this is: B/E|hrr|HR-hrm<br />
     * The other way is to provide training experience, rest heart rate and age. Pattern for this is: B/E|hrr|A-age.
     * This way an estimated maximum heart rate is calculated based on age using the Fox method(220-age).<br />
     * Training experience is needed cause the training zones differs a bit if it's an experienced user
     * or basic user. Experienced use zoning;
     * z5:95-100% of hrMax
     * z4:85-95%
     * z3:75-85%
     * z2:60-75%
     * z1:50-60%
     * Basic on the other hand uses zoning;
     * z5:90-100%
     * z4:80-90%
     * z3:70-80%
     * z2:60-70%
     * z1:50-60%.
     * As the Karvonen method is used to calculate the different zones are used it's also needed to provide
     * resting heart rate.<br />
     * Validation of the data is being to check that it's within reasonable ranges, following values are valid:
     * training experience: B or E
     * resting heart rate: a numeric value between 26 and 100
     * maximum heart rate: a numeric value between 120 and 220
     * age: a numeric value between 10 and 99
     * The returned string is Json formatted and contains the different training heart zones with it's corresponding
     *
     * @param heartRateCalculationData Combined value of data needed for calculation
     * @return Json formatted string with the different training heart zones
     */
    public static String calculateHeartRateZones(String heartRateCalculationData) {
        return HeartRateCalculationData.parse(heartRateCalculationData).getHeartRateZones();
    }

    static class HeartRateCalculationData {
        final int hrRest;
        final int hrMax;
        final boolean experienced;

        private HeartRateCalculationData(int hrRest, int hrMax, boolean experienced) {
            this.hrRest = hrRest;
            this.hrMax = hrMax;
            this.experienced = experienced;
        }

        static HeartRateCalculationData parse(String heartRateCalculationData) {
            if (heartRateCalculationData != null) {
                if (heartRateCalculationData.isEmpty()) {
                    return new HeartRateCalculationData(0, 0, false);
                }

                if (heartRateCalculationData.matches(TRAINING_EXPERIENCE_HRREST_HRMAX_PATTERN) ||   // B/E|hrRest|HR-hrMax
                        heartRateCalculationData.matches(TRAINING_EXPERIENCE_HRREST_AGE_PATTERN)) {  // B/E|hrRest|A-age
                    String[] split = heartRateCalculationData.split("\\|");
                    String[] subSplit = split[2].split("-");
                    String hrMaxOrAge = subSplit[1]; // hrm or age
                    boolean experienced = split[0].equalsIgnoreCase("E");
                    int hrRest = RestingHeartRate.parse(split[1]).getHeartRate();
                    int hrMax;

                    if (heartRateCalculationData.matches(TRAINING_EXPERIENCE_HRREST_HRMAX_PATTERN)) {
                        hrMax = MaximumHeartRate.parse(hrMaxOrAge).getHeartRate();
                    } else { // age
                        hrMax = 220 - Age.parse(hrMaxOrAge).getAge(); // fox method
                    }

                    return new HeartRateCalculationData(hrRest, hrMax, experienced);
                }
            }

            throw new IllegalArgumentException();
        }

        private int toHeartRate(int percent) {
            return (int) Math.round(((hrMax - hrRest) * (percent * 0.01)) + hrRest); // karvonen method
        }

        String getHeartRateZones() {
            HeartRateZones heartRateZones = new HeartRateZones();
            if (experienced) {
                heartRateZones.addZone(HeartRateZone.create(ZONE_5, toHeartRate(95), hrMax, 95, 100));
                heartRateZones.addZone(HeartRateZone.create(ZONE_4, toHeartRate(85), toHeartRate(95) - 1, 85, 95));
                heartRateZones.addZone(HeartRateZone.create(ZONE_3, toHeartRate(75), toHeartRate(85) - 1, 75, 85));
                heartRateZones.addZone(HeartRateZone.create(ZONE_2, toHeartRate(60), toHeartRate(75) - 1, 60, 75));
            } else {
                heartRateZones.addZone(HeartRateZone.create(ZONE_5, toHeartRate(90), hrMax, 90, 100));
                heartRateZones.addZone(HeartRateZone.create(ZONE_4, toHeartRate(80), toHeartRate(90) - 1, 80, 90));
                heartRateZones.addZone(HeartRateZone.create(ZONE_3, toHeartRate(70), toHeartRate(80) - 1, 70, 80));
                heartRateZones.addZone(HeartRateZone.create(ZONE_2, toHeartRate(60), toHeartRate(70) - 1, 60, 70));
            }

            heartRateZones.addZone(HeartRateZone.create(ZONE_1, toHeartRate(50), toHeartRate(60) - 1, 50, 60));

            return new GsonBuilder().create().toJson(heartRateZones);
        }
    }

    public static class MaximumHeartRate {
        final int heartRate;

        private MaximumHeartRate(int heartRate) {
            this.heartRate = heartRate;
        }

        public static MaximumHeartRate parse(String hrMax) {
            if (hrMax != null && !hrMax.isEmpty()) {
                int heartRate = Integer.parseInt(hrMax);
                if (heartRate > 220 || heartRate < 120) { // Hmm, tachycardia next or magically low mhr
                    throw new IllegalArgumentException();
                }
                return new MaximumHeartRate(heartRate);
            }
            throw new IllegalArgumentException();
        }

        int getHeartRate() {
            return heartRate;
        }
    }

    public static class RestingHeartRate {
        final int heartRate;

        private RestingHeartRate(int heartRate) {
            this.heartRate = heartRate;
        }

        public static RestingHeartRate parse(String hrRest) {
            if (hrRest != null && !hrRest.isEmpty()) {
                int heartRate = Integer.parseInt(hrRest);
                if (heartRate < 26 || heartRate > 100) { // world record in lowest resting heart beat broken or you should train more
                    throw new IllegalArgumentException();
                }
                return new RestingHeartRate(heartRate);
            }
            throw new IllegalArgumentException();
        }

        int getHeartRate() {
            return heartRate;
        }
    }

    public static class Age {
        final int age;

        private Age(int age) {
            this.age = age;
        }

        public static Age parse(String ageStr) {
            if (ageStr != null && !ageStr.isEmpty()) {
                int age = Integer.parseInt(ageStr);
                if (age < 10 || age > 99) { // sorry all kids and 99+ seniors
                    throw new IllegalArgumentException();
                }
                return new Age(age);
            }
            throw new IllegalArgumentException();
        }

        int getAge() {
            return age;
        }
    }

    public static class Speed {
        private final MatchedDouble speed;

        private Speed(MatchedDouble speed) {
            this.speed = speed;
        }

        public static Speed parse(String speed) {
            return new Speed(MatchedDouble.parse(speed));
        }

        String asPace() {
            int minutes = 0;
            int seconds = 0;
            if (speed.getValue() != 0) {
                double remainder = SECONDS_IN_HOUR / speed.getValue();
                minutes = (int) remainder / 60;
                seconds = (int) Math.round(remainder - minutes * 60);
            }
            return String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds);
        }
    }

    public static class Distance {
        private final MatchedDouble distance;

        private Distance(MatchedDouble distance) {
            this.distance = distance;
        }

        public static Distance parse(String distance) {
            return new Distance(MatchedDouble.parse(distance));
        }

        double getDistance() {
            return distance.getValue();
        }

        boolean hasValue() {
            return distance.hasValue();
        }
    }

    public static class Pace {
        private final int minutes;
        private final int seconds;

        private Pace(int minutes, int seconds) {
            this.minutes = minutes;
            this.seconds = seconds;
        }

        public static Pace parse(String pace) {
            if (pace != null) {
                if (pace.isEmpty()) {
                    return new Pace(0, 0);
                }
                if (pace.matches(PACE_PATTERN)) {
                    int minutes;
                    int seconds = 0;

                    if (pace.contains(":")) { // mm:ss
                        String[] split = pace.split(":");
                        minutes = Integer.parseInt(split[0]);
                        if (split.length > 1) {
                            seconds = Integer.parseInt(split[1]);
                        }
                    } else { // mm
                        minutes = Integer.parseInt(pace);
                    }

                    if (seconds > 59) {
                        throw new IllegalArgumentException();
                    }

                    return new Pace(minutes, seconds);
                }
            }
            throw new IllegalArgumentException();
        }

        static String parseSeconds(int seconds) {
            int minutes = 0;

            if (seconds > 0) {
                minutes = seconds / 60;
                seconds = seconds - (minutes * 60);
            } else {
                seconds = 0;
            }

            return String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds);
        }

        String asSpeed() {
            double speed = 0.0;
            if (inMinutes() > 0) {
                speed = 60 / inMinutes();
            }
            return String.format(Locale.ENGLISH, "%.2f", speed);
        }

        double inMinutes() {
            return (seconds * SECOND_IN_MINUTE) + minutes;
        }

        int inSeconds() {
            return (minutes * 60) + seconds;
        }

        boolean hasValue() {
            return (minutes > -1 && seconds > -1) &&
                    (minutes > 0 || seconds > 0);
        }
    }

    public static class Time {
        private final int hours;
        private final int minutes;
        private final int seconds;

        private Time(int hours, int minutes, int seconds) {
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }

        public static Time parse(String time) {
            if (time != null) {
                if (time.isEmpty()) {
                    return new Time(0, 0, 0);
                }

                int hours = 0;
                int minutes;
                int seconds;

                if (time.matches(TIME_PATTERN)) {
                    String[] split = time.split(":");
                    int i = 0;
                    if (split.length > 2) { // hh:mm:ss
                        hours = Integer.parseInt(split[i++]);
                    } // mm:ss

                    minutes = Integer.parseInt(split[i++]);
                    seconds = Integer.parseInt(split[i]);

                    if (hours > 23 || minutes > 59 || seconds > 59) {
                        throw new IllegalArgumentException();
                    }

                    return new Time(hours, minutes, seconds);
                }
            }
            throw new IllegalArgumentException();
        }

        static String parseSeconds(int seconds) {
            int hours = 0;
            int minutes = 0;

            if (seconds > 0) {
                hours = seconds / SECONDS_IN_HOUR;
                minutes = (seconds - hours * SECONDS_IN_HOUR) / 60;
                seconds = Math.round((seconds - hours * SECONDS_IN_HOUR) - minutes * 60);
            } else {
                seconds = 0;
            }

            return String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds);
        }

        int inSeconds() {
            return (hours * 60 * 60) + (minutes * 60) + seconds;
        }

        boolean hasValue() {
            return (hours > -1 && minutes > -1 && seconds > -1) &&
                    (hours > 0 || minutes > 0 || seconds > 0);
        }
    }

    private static class MatchedDouble {
        private final double value;

        private MatchedDouble(double value) {
            this.value = value;
        }

        static MatchedDouble parse(String str) {
            if (str != null) {
                if (str.isEmpty()) {
                    return new MatchedDouble(Double.parseDouble("0"));
                }
                if (str.matches(SPEED_DISTANCE_PATTERN)) {
                    return new MatchedDouble(Double.parseDouble(str));
                }
            }
            throw new IllegalArgumentException();
        }

        double getValue() {
            return value;
        }

        boolean hasValue() {
            return value > 0;
        }
    }

    private static void throwExceptionIfMalformedStringPattern(String combined) {
        if (combined == null || combined.isEmpty() || !combined.matches(SIMPLE_COMBINED_STRING_PATTERN)) {
            throw new IllegalArgumentException();
        }
    }
}
