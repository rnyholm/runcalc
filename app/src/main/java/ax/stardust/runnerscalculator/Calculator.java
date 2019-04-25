package ax.stardust.runnerscalculator;

import java.text.DecimalFormat;
import java.util.Locale;

public class Calculator {
    private static final int SECONDS_IN_HOUR = 3600;

    private static final double SECOND_IN_MINUTE = 0.0166666667;
    private static final double KM_IN_MILES = 0.621371192;

    private static final String PACE_PATTERN = "^[0-9]*$|^[0-9]*:[0-9]*$";
    private static final String SPEED_PATTERN = "^[0-9]*$|^[0-9]*\\.[0-9]*$";
    private static final String TIME_PATTERN = "^[0-9]{1,2}:[0-9]{1,2}$|^[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}$";

    public static String convertPaceToSpeed(String pace) {
        return Pace.parse(pace).asSpeed();
    }

    public static String convertSpeedToPace(String speed) {
        return Speed.parse(speed).asPace();
    }

    public static String calculatePace(String s) {
        return "";
    }

    public static String calculateTime(String s) {
        return "";
    }

    public static String calculateDistance(String s) {
        return "";
    }

    private static class Speed {
        private double speed;

        private Speed(double speed) {
            this.speed = speed;
        }

        static Speed parse(String speed) {
            if (speed != null) {
                if (speed.isEmpty()) {
                    return new Speed(Double.parseDouble("0"));
                }
                if (speed.matches(SPEED_PATTERN)) {
                    return new Speed(Double.parseDouble(speed));
                }
            }

            throw new IllegalArgumentException();
        }

        String asPace() {
            int minutes = 0;
            int seconds = 0;
            if (speed != 0) {
                double remainder =  SECONDS_IN_HOUR / speed;
                minutes = (int) remainder / 60;
                seconds = (int) Math.round(remainder - minutes * 60);
            }
            return String.format(Locale.ENGLISH, "%d:%02d", minutes, seconds);
        }
    }

    private static class Pace {
        private int minutes;
        private int seconds;

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
                    String minutesString;
                    String secondsString = "00";

                    if (pace.contains(":")) { // mm:ss
                        String[] split = pace.split(":");
                        minutesString = split[0];
                        if (split.length > 1) {
                            secondsString = split[1];
                        }
                    } else { // mm
                        minutesString = pace;
                    }

                    int minutes = Integer.parseInt(minutesString);
                    int seconds = Integer.parseInt(secondsString);
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
}
