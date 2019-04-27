package ax.stardust.runcal;

import java.util.Locale;

class Calculator {
    private static final int SECONDS_IN_HOUR = 3600;

    private static final double SECOND_IN_MINUTE = 0.0166666667;
    private static final double KM_IN_MILES = 0.621371192;

    private static final String PACE_PATTERN = "^[0-9]*$|^[0-9]*:[0-9]*$";
    private static final String SPEED_DISTANCE_PATTERN = "^[0-9]*$|^[0-9]*\\.[0-9]*$";
    private static final String TIME_PATTERN = "^[0-9]{1,2}:[0-9]{1,2}$|^[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}$";
    private static final String COMBINED_STRING_PATTERN = "^[^\\|]+\\|[^\\|]+$";

    static String convertPaceToSpeed(String pace) {
        return Pace.parse(pace).asSpeed();
    }

    static String convertSpeedToPace(String speed) {
        return Speed.parse(speed).asPace();
    }

    static String calculatePace(String distanceAndTime) {
        throwExceptionIfMalformedStringPattern(distanceAndTime);
        String[] split = distanceAndTime.split("\\|");

        Distance distance = Distance.parse(split[0]);
        Time time = Time.parse(split[1]);

        int paceInSeconds = 0;
        if (!distance.isZero() && !time.isZero()) {
            paceInSeconds = (int) Math.round(time.inSeconds() / distance.getDistance());
        }

        return Time.asPace(paceInSeconds);
    }

    static String calculateTime(String s) {
        return "";
    }

    static String calculateDistance(String s) {
        return "";
    }

    private static void throwExceptionIfMalformedStringPattern(String combined) {
        if (combined == null || combined.isEmpty() || !combined.matches(COMBINED_STRING_PATTERN)) {
            throw new IllegalArgumentException();
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

        boolean isZero() {
            return value == 0;
        }
    }

    private static class Speed {
        private final MatchedDouble speed;

        private Speed(MatchedDouble speed) {
            this.speed = speed;
        }

        static Speed parse(String speed) {
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

    private static class Distance {
        private final MatchedDouble distance;

        private Distance(MatchedDouble distance) {
            this.distance = distance;
        }

        static Distance parse(String distance) {
            return new Distance(MatchedDouble.parse(distance));
        }

        double getDistance() {
            return distance.getValue();
        }

        boolean isZero() {
            return distance.isZero();
        }
    }

    private static class Pace {
        private final int minutes;
        private final int seconds;

        private Pace(int minutes, int seconds) {
            this.minutes = minutes;
            this.seconds = seconds;
        }

        static Pace parse(String pace) {
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

        double inMinutes() {
            return (seconds * SECOND_IN_MINUTE) + minutes;
        }

        String asSpeed() {
            double speed = 0.0;
            if (inMinutes() > 0) {
                speed = 60 / inMinutes();
            }
            return String.format(Locale.ENGLISH, "%.2f", speed);
        }
    }

    private static class Time {
        private final int hours;
        private final int minutes;
        private final int seconds;

        private Time(int hours, int minutes, int seconds) {
            this.hours = hours;
            this.minutes = minutes;
            this.seconds = seconds;
        }

        static Time parse(String time) {
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

        int inSeconds() {
            return (hours * 60 * 60) + (minutes * 60) + seconds;
        }

        static String asPace(int seconds) {
            int minutes = 0;
            if (seconds != 0) {
                minutes = seconds / 60;
                seconds = seconds - (minutes * 60);
            }

            return String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds);
        }

        boolean isZero() {
            return hours == 0 && minutes == 0 && seconds == 0;
        }
    }
}
