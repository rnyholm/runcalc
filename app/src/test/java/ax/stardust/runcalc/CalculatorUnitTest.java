package ax.stardust.runcalc;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


@SuppressWarnings("Duplicates")
public class CalculatorUnitTest {
    private final String PACE_0 = "0";
    private final String PACE_0_0 = "0:0";
    private final String PACE_0_00 = "0:00";
    private final String PACE_0_18 = "0:18";
    private final String PACE_3_14 = "3:14";
    private final String PACE_5_10 = "5:10";
    private final String PACE_5_00 = "5:00";
    private final String PACE_6_00 = "6:00";
    private final String PACE_7_21 = "7:21";
    private final String PACE_8_19 = "8:19";
    private final String PACE_10_00 = "10:00";
    private final String PACE_60_00 = "60:00";
    private final String PACE_600_00 = "600:00";

    private final String SPEED_DISTANCE_0 = "0";
    private final String SPEED_DISTANCE_0_0 = "0.0";
    private final String SPEED_DISTANCE_0_00 = "0.00";
    private final String SPEED_DISTANCE_0_1 = "0.1";
    private final String SPEED_DISTANCE_0_10 = "0.10";
    private final String SPEED_DISTANCE_1 = "1";
    private final String SPEED_DISTANCE_1_00 = "1.00";
    private final String SPEED_DISTANCE_6_00 = "6.00";
    private final String SPEED_DISTANCE_7_21 = "7.21";
    private final String SPEED_DISTANCE_8_16 = "8.16";
    private final String SPEED_DISTANCE_10_00 = "10.00";
    private final String SPEED_DISTANCE_11_61 = "11.61";
    private final String SPEED_DISTANCE_12 = "12";
    private final String SPEED_DISTANCE_12_00 = "12.00";
    private final String SPEED_DISTANCE_18_56 = "18.56";
    private final String SPEED_DISTANCE_200_00 = "200.00";

    private final String TIME_00_00_00 = "00:00:00";
    private final String TIME_00_03_29 = "00:03:29";
    private final String TIME_00_26_23 = "00:26:23";
    private final String TIME_00_42_10 = "00:42:10";
    private final String TIME_00_59_58 = "00:59:58";
    private final String TIME_00_59_59 = "00:59:59";
    private final String TIME_01_00_00 = "01:00:00";
    private final String TIME_00_00 = "00:00";

    private int exceptionCounter = 0;

    @Before
    public void before() {
        exceptionCounter = 0;
    }

    @Test
    public void testConvertSpeedToPace() {
        String SPEED_DISTANCE_EMPTY = "";
        String PACE_66_40 = "66:40";
        String SPEED_DISTANCE__9 = ".9";
        String PACE_7_30 = "7:30";
        String SPEED_DISTANCE_8_ = "8.";

        assertEquals(PACE_0_00, Calculator.convertSpeedToPace(SPEED_DISTANCE_EMPTY));
        assertEquals(PACE_0_00, Calculator.convertSpeedToPace(SPEED_DISTANCE_0));
        assertEquals(PACE_0_00, Calculator.convertSpeedToPace(SPEED_DISTANCE_0_0));
        assertEquals(PACE_0_00, Calculator.convertSpeedToPace(SPEED_DISTANCE_0_00));
        assertEquals(PACE_600_00, Calculator.convertSpeedToPace(SPEED_DISTANCE_0_1));
        assertEquals(PACE_66_40, Calculator.convertSpeedToPace(SPEED_DISTANCE__9));
        assertEquals(PACE_60_00, Calculator.convertSpeedToPace(SPEED_DISTANCE_1));
        assertEquals(PACE_10_00, Calculator.convertSpeedToPace(SPEED_DISTANCE_6_00));
        assertEquals(PACE_8_19, Calculator.convertSpeedToPace(SPEED_DISTANCE_7_21));
        assertEquals(PACE_7_30, Calculator.convertSpeedToPace(SPEED_DISTANCE_8_));
        assertEquals(PACE_7_21, Calculator.convertSpeedToPace(SPEED_DISTANCE_8_16));
        assertEquals(PACE_6_00, Calculator.convertSpeedToPace(SPEED_DISTANCE_10_00));
        assertEquals(PACE_5_10, Calculator.convertSpeedToPace(SPEED_DISTANCE_11_61));
        assertEquals(PACE_5_00, Calculator.convertSpeedToPace(SPEED_DISTANCE_12));
        assertEquals(PACE_3_14, Calculator.convertSpeedToPace(SPEED_DISTANCE_18_56));
        assertEquals(PACE_0_18, Calculator.convertSpeedToPace(SPEED_DISTANCE_200_00));
    }

    @Test
    public void testConvertPaceToSpeed() {
        String PACE_EMPTY = "";
        String PACE_03_14 = "03:14";
        String PACE_5 = "5";
        String PACE_5_ = "5:";
        String PACE_6 = "6";
        String PACE_07_21 = "07:21";

        assertEquals(SPEED_DISTANCE_0_00, Calculator.convertPaceToSpeed(PACE_EMPTY));
        assertEquals(SPEED_DISTANCE_0_00, Calculator.convertPaceToSpeed(PACE_0));
        assertEquals(SPEED_DISTANCE_0_00, Calculator.convertPaceToSpeed(PACE_0_0));
        assertEquals(SPEED_DISTANCE_0_00, Calculator.convertPaceToSpeed(PACE_0_00));
        assertEquals(SPEED_DISTANCE_200_00, Calculator.convertPaceToSpeed(PACE_0_18));
        assertEquals(SPEED_DISTANCE_18_56, Calculator.convertPaceToSpeed(PACE_03_14));
        assertEquals(SPEED_DISTANCE_12_00, Calculator.convertPaceToSpeed(PACE_5));
        assertEquals(SPEED_DISTANCE_12_00, Calculator.convertPaceToSpeed(PACE_5_));
        assertEquals(SPEED_DISTANCE_11_61, Calculator.convertPaceToSpeed(PACE_5_10));
        assertEquals(SPEED_DISTANCE_10_00, Calculator.convertPaceToSpeed(PACE_6));
        assertEquals(SPEED_DISTANCE_8_16, Calculator.convertPaceToSpeed(PACE_07_21));
        assertEquals(SPEED_DISTANCE_7_21, Calculator.convertPaceToSpeed(PACE_8_19));
        assertEquals(SPEED_DISTANCE_6_00, Calculator.convertPaceToSpeed(PACE_10_00));
        assertEquals(SPEED_DISTANCE_1_00, Calculator.convertPaceToSpeed(PACE_60_00));
        assertEquals(SPEED_DISTANCE_0_10, Calculator.convertPaceToSpeed(PACE_600_00));
    }

    @Test
    public void testCalculatePace() {
        String TIME_3_29 = "3:29";
        String TIME_26_23 = "26:23";
        String TIME_00_30_59 = "00:30:59";
        String TIME_42_10 = "42:10";
        String TIME_59_59 = "59:59";
        String TIME_1_0_0 = "1:0:0";

        assertEquals(PACE_0_00, Calculator.calculatePace(combineStrings(SPEED_DISTANCE_0_0, TIME_00_00)));
        assertEquals(PACE_0_00, Calculator.calculatePace(combineStrings(SPEED_DISTANCE_1, TIME_00_00_00)));
        assertEquals(PACE_0_18, Calculator.calculatePace(combineStrings(SPEED_DISTANCE_11_61, TIME_3_29)));
        assertEquals(PACE_3_14, Calculator.calculatePace(combineStrings(SPEED_DISTANCE_8_16, TIME_26_23)));
        assertEquals(PACE_0_00, Calculator.calculatePace(combineStrings(SPEED_DISTANCE_0, TIME_00_30_59)));
        assertEquals(PACE_5_10, Calculator.calculatePace(combineStrings(SPEED_DISTANCE_8_16, TIME_42_10)));
        assertEquals(PACE_8_19, Calculator.calculatePace(combineStrings(SPEED_DISTANCE_7_21, TIME_59_59)));
        assertEquals(PACE_7_21, Calculator.calculatePace(combineStrings(SPEED_DISTANCE_8_16, TIME_00_59_58)));
        assertEquals(PACE_6_00, Calculator.calculatePace(combineStrings(SPEED_DISTANCE_10_00, TIME_1_0_0)));
        assertEquals(PACE_5_00, Calculator.calculatePace(combineStrings(SPEED_DISTANCE_12, TIME_1_0_0)));
        assertEquals(PACE_60_00, Calculator.calculatePace(combineStrings(SPEED_DISTANCE_1, TIME_01_00_00)));
        assertEquals(PACE_600_00, Calculator.calculatePace(combineStrings(SPEED_DISTANCE_0_1, TIME_01_00_00)));
        assertEquals(PACE_5_00, Calculator.calculatePace(combineStrings(SPEED_DISTANCE_12, TIME_01_00_00)));
    }

    @Test
    public void testCalculateTime() {
        assertEquals(TIME_00_00_00, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_0_0, PACE_0)));
        assertEquals(TIME_00_00_00, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_0_0, PACE_0_0)));
        assertEquals(TIME_00_00_00, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_7_21, PACE_0_0)));
        assertEquals(TIME_00_00_00, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_0_0, PACE_0_00)));
        assertEquals(TIME_00_03_29, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_11_61, PACE_0_18)));
        assertEquals(TIME_00_00_00, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_0, PACE_3_14)));
        assertEquals(TIME_00_26_23, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_8_16, PACE_3_14)));
        assertEquals(TIME_01_00_00, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_12, PACE_5_00)));
        assertEquals(TIME_00_42_10, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_8_16, PACE_5_10)));
        assertEquals(TIME_01_00_00, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_10_00, PACE_6_00)));
        assertEquals(TIME_00_59_59, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_8_16, PACE_7_21)));
        assertEquals(TIME_00_59_58, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_7_21, PACE_8_19)));
        assertEquals(TIME_01_00_00, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_1, PACE_60_00)));
        assertEquals(TIME_01_00_00, Calculator.calculateTime(combineStrings(SPEED_DISTANCE_0_1, PACE_600_00)));
    }

    @Test
    public void testCalculateDistance() {
        assertEquals(SPEED_DISTANCE_0_00, Calculator.calculateDistance(combineStrings(TIME_00_00, PACE_0)));
        assertEquals(SPEED_DISTANCE_0_00, Calculator.calculateDistance(combineStrings(TIME_00_03_29, PACE_0_0)));
        assertEquals(SPEED_DISTANCE_0_00, Calculator.calculateDistance(combineStrings(TIME_00_00_00, PACE_0_00)));
        assertEquals(SPEED_DISTANCE_11_61, Calculator.calculateDistance(combineStrings(TIME_00_03_29, PACE_0_18)));
        assertEquals(SPEED_DISTANCE_0_00, Calculator.calculateDistance(combineStrings(TIME_00_00_00, PACE_3_14)));
        assertEquals(SPEED_DISTANCE_8_16, Calculator.calculateDistance(combineStrings(TIME_00_26_23, PACE_3_14)));
        assertEquals(SPEED_DISTANCE_12_00, Calculator.calculateDistance(combineStrings(TIME_01_00_00, PACE_5_00)));
        assertEquals(SPEED_DISTANCE_8_16, Calculator.calculateDistance(combineStrings(TIME_00_42_10, PACE_5_10)));
        assertEquals(SPEED_DISTANCE_10_00, Calculator.calculateDistance(combineStrings(TIME_01_00_00, PACE_6_00)));
        assertEquals(SPEED_DISTANCE_8_16, Calculator.calculateDistance(combineStrings(TIME_00_59_59, PACE_7_21)));
        assertEquals(SPEED_DISTANCE_7_21, Calculator.calculateDistance(combineStrings(TIME_00_59_58, PACE_8_19)));
        assertEquals(SPEED_DISTANCE_1_00, Calculator.calculateDistance(combineStrings(TIME_01_00_00, PACE_60_00)));
        assertEquals(SPEED_DISTANCE_0_10, Calculator.calculateDistance(combineStrings(TIME_01_00_00, PACE_600_00)));
    }

    @Test
    public void testConvertPaceToSpeedExceptions() {
        try {
            Calculator.convertPaceToSpeed("-10:0");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertPaceToSpeed(":10");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertPaceToSpeed(":");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertPaceToSpeed("5:60");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertPaceToSpeed("XX:YY");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertPaceToSpeed("5:-1");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertPaceToSpeed("3f:-1");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertPaceToSpeed("l-:01");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertPaceToSpeed("100:75");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        assertEquals(9, exceptionCounter);
    }

    @Test
    public void testConvertSpeedToPaceExceptions() {
        try {
            Calculator.convertSpeedToPace("f.9");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertSpeedToPace("-10.9");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertSpeedToPace("10,9");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertSpeedToPace("10:00");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertSpeedToPace("5.-9");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertSpeedToPace("6.kl");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.convertSpeedToPace("xx.yy");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        assertEquals(7, exceptionCounter);
    }

    @Test
    public void testCalculatePaceExceptions() {
        try {
            Calculator.calculatePace("5.023:05");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculatePace("5.0||23:05");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculatePace("|23:05");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculatePace("5.0|");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculatePace("5.0|01");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculatePace("5.0|00:00:00:00");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculatePace("5.0|00:0d:-1");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculatePace("5.0|f:-1");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculatePace("5.0|00:60");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculatePace("5.0|60:00");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculatePace("5.0|24:00:00");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculatePace("5.0|-1:-2:-3");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        assertEquals(12, exceptionCounter);
    }

    @Test
    public void testCalculateTimeExceptions() {
        try {
            Calculator.calculateTime("5.023:05");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateTime("5.0||5:19");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateTime("|04:1");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateTime("7.0|");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateTime("4.2|-2");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateTime("6.5|00:06:23");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateTime("8.2|0d:-1");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateTime("19.3|g:-2");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateTime("7.9|5:60");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateTime("5.4|-1:-2");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        assertEquals(10, exceptionCounter);
    }

    @Test
    public void testCalculateDistanceExceptions() {
        try {
            Calculator.calculateDistance("23:055:0");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateDistance("23:05||5:19");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateDistance("|4:1");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateDistance("01:09:10|");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateDistance("10:54|-2:5");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateDistance("00:06:23|6.5");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateDistance("0h:-6|8:13");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateDistance("00:00:60|5:13");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateDistance("00:60:00|5:45");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateDistance("24:00:00|4:44");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateDistance("23:05:59|6:-45");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            Calculator.calculateDistance("23:05:59|-7:34");
            fail();
        } catch (Exception e) {
            exceptionCounter++;
        }

        assertEquals(12, exceptionCounter);
    }

    private String combineStrings(String s1, String s2) {
        return s1 + "|" + s2;
    }
}