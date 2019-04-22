package ax.stardust.runnerscalculator;

public class CalculatorUtils {
    private static final double SECOND_IN_MINUTE = 0.0166666667;
    private static final double KM_IN_MILES = 0.621371192;

    private static final String PACE_PATTERN = "^[0-9]*$|^[0-9]*:[0-9]*$";
    private static final String SPEED_PATTERN = "^[0-9]*$|^[0-9]*.[0-9]*$";
    private static final String TIME_PATTERN = "^[0-9]{1,2}:[0-9]{1,2}$|^[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}$";

    public static String convertPaceToSpeed(Measurement measurement, String paceString) {
        Pace pace = Pace.parse(paceString);
        double speed = 60/pace.inMinutes();
        if (Measurement.IMPERIAL.equals(measurement)) {
            speed *= KM_IN_MILES;
        }

        return String.format("%.2f", speed);
    }

    private static class Pace {
        private int minutes;
        private int seconds;


        private Pace(int minutes, int seconds) {
            this.minutes = minutes;
            this.seconds = seconds;
        }

        static Pace parse(String paceString) {
            if (paceString != null && !paceString.isEmpty()) {
                if (paceString.matches(PACE_PATTERN)) {
                    String minutesString;
                    String secondsString = "00";

                    if (paceString.contains(":")) { // mm:ss
                        String[] split = paceString.split(":");
                        minutesString = split[0];
                        secondsString = split[1];
                    } else { // mm
                        minutesString = paceString;
                    }

                    int minutes = Integer.parseInt(minutesString);
                    int seconds = Integer.parseInt(secondsString);
                    if (seconds > 59) {
                        throw new RuntimeException();
                    }

                    return new Pace(minutes, seconds);
                }
            }

            throw new RuntimeException();
        }

        double inMinutes() {
            return (seconds * SECOND_IN_MINUTE) + minutes;
        }

        int inSeconds() {
            return (minutes * 60) + seconds;
        }
    }
}
