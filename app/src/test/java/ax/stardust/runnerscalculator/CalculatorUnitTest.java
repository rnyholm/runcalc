package ax.stardust.runnerscalculator;

import org.junit.Test;

import static org.junit.Assert.*;


public class CalculatorUnitTest {
    private final String PACE_EMPTY = "";
    private final String PACE_0 = "0";
    private final String PACE_0_0 = "0:0";
    private final String PACE_0_00 = "0:00";
    private final String PACE_0_18 = "0:18";
    private final String PACE_3_14 = "3:14";
    private final String PACE_03_14 = "03:14";
    private final String PACE_5 = "5";
    private final String PACE_5_ = "5:";
    private final String PACE_5_10 = "5:10";
    private final String PACE_5_00 = "5:00";
    private final String PACE_6 = "6";
    private final String PACE_6_00 = "6:00";
    private final String PACE_7_21 = "7:21";
    private final String PACE_07_21 = "07:21";
    private final String PACE_7_30 = "7:30";
    private final String PACE_8_19 = "8:19";
    private final String PACE_10_00 = "10:00";
    private final String PACE_60_00 = "60:00";
    private final String PACE_66_40 = "66:40";
    private final String PACE_600_00 = "600:00";

    private final String SPEED_EMPTY = "";
    private final String SPEED_0 = "0";
    private final String SPEED_0_0 = "0.0";
    private final String SPEED_0_00 = "0.00";
    private final String SPEED_0_1 = "0.1";
    private final String SPEED__9 = ".9";
    private final String SPEED_0_10 = "0.10";
    private final String SPEED_1 = "1";
    private final String SPEED_1_00 = "1.00";
    private final String SPEED_6_00 = "6.00";
    private final String SPEED_7_21 = "7.21";
    private final String SPEED_8_ = "8.";
    private final String SPEED_8_16 = "8.16";
    private final String SPEED_10_00 = "10.00";
    private final String SPEED_11_61 = "11.61";
    private final String SPEED_12 = "12";
    private final String SPEED_12_00 = "12.00";
    private final String SPEED_18_56 = "18.56";
    private final String SPEED_200_00 = "200.00";

    @Test
    public void testConvertSpeedToPace() {
        assertEquals(PACE_0_00, Calculator.convertSpeedToPace(SPEED_EMPTY));
        assertEquals(PACE_0_00, Calculator.convertSpeedToPace(SPEED_0));
        assertEquals(PACE_0_00, Calculator.convertSpeedToPace(SPEED_0_0));
        assertEquals(PACE_0_00, Calculator.convertSpeedToPace(SPEED_0_00));
        assertEquals(PACE_600_00, Calculator.convertSpeedToPace(SPEED_0_1));
        assertEquals(PACE_66_40, Calculator.convertSpeedToPace(SPEED__9));
        assertEquals(PACE_60_00, Calculator.convertSpeedToPace(SPEED_1));
        assertEquals(PACE_10_00, Calculator.convertSpeedToPace(SPEED_6_00));
        assertEquals(PACE_8_19, Calculator.convertSpeedToPace(SPEED_7_21));
        assertEquals(PACE_7_30, Calculator.convertSpeedToPace(SPEED_8_));
        assertEquals(PACE_7_21, Calculator.convertSpeedToPace(SPEED_8_16));
        assertEquals(PACE_6_00, Calculator.convertSpeedToPace(SPEED_10_00));
        assertEquals(PACE_5_10, Calculator.convertSpeedToPace(SPEED_11_61));
        assertEquals(PACE_5_00, Calculator.convertSpeedToPace(SPEED_12));
        assertEquals(PACE_3_14, Calculator.convertSpeedToPace(SPEED_18_56));
        assertEquals(PACE_0_18, Calculator.convertSpeedToPace(SPEED_200_00));
    }

    @Test
    public void testConvertPaceToSpeed() {
        assertEquals(SPEED_0_00, Calculator.convertPaceToSpeed(PACE_EMPTY));
        assertEquals(SPEED_0_00, Calculator.convertPaceToSpeed(PACE_0));
        assertEquals(SPEED_0_00, Calculator.convertPaceToSpeed(PACE_0_0));
        assertEquals(SPEED_0_00, Calculator.convertPaceToSpeed(PACE_0_00));
        assertEquals(SPEED_200_00, Calculator.convertPaceToSpeed(PACE_0_18));
        assertEquals(SPEED_18_56, Calculator.convertPaceToSpeed(PACE_03_14));
        assertEquals(SPEED_12_00, Calculator.convertPaceToSpeed(PACE_5));
        assertEquals(SPEED_12_00, Calculator.convertPaceToSpeed(PACE_5_));
        assertEquals(SPEED_11_61, Calculator.convertPaceToSpeed(PACE_5_10));
        assertEquals(SPEED_10_00, Calculator.convertPaceToSpeed(PACE_6));
        assertEquals(SPEED_8_16, Calculator.convertPaceToSpeed(PACE_07_21));
        assertEquals(SPEED_7_21, Calculator.convertPaceToSpeed(PACE_8_19));
        assertEquals(SPEED_6_00, Calculator.convertPaceToSpeed(PACE_10_00));
        assertEquals(SPEED_1_00, Calculator.convertPaceToSpeed(PACE_60_00));
        assertEquals(SPEED_0_10, Calculator.convertPaceToSpeed(PACE_600_00));
    }

    @Test
    public void testCalculatePace() {
        Calculator.calculatePace("5.0|23:05");
    }

    @Test
    public void testCalculatePaceExceptions() {
        int exceptionCounter = 0;

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

        assertEquals(2, exceptionCounter);
    }

    @Test
    public void testConvertSpeedToPaceExceptions() {
        int exceptionCounter = 0;

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
    public void testConvertPaceToSpeedExceptions() {
        int exceptionCounter = 0;

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
}