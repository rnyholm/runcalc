package ax.stardust.runcalc.util;

import java.util.Locale;

public class Calculator {
    private static final int SECONDS_IN_HOUR = 3600;

    private static final double SECOND_IN_MINUTE = 0.0166666667;

    private static final String PACE_PATTERN = "^[0-9]*$|^[0-9]*:[0-9]*$";
    private static final String SPEED_DISTANCE_PATTERN = "^[0-9]*$|^[0-9]*\\.[0-9]*$";
    private static final String TIME_PATTERN = "^[0-9]{1,2}:[0-9]{1,2}$|^[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}$";
    private static final String COMBINED_STRING_PATTERN = "^[^|]+\\|[^|]+$";

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
        if (combined == null || combined.isEmpty() || !combined.matches(COMBINED_STRING_PATTERN)) {
            throw new IllegalArgumentException();
        }
    }
}
