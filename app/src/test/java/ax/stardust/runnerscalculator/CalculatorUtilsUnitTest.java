package ax.stardust.runnerscalculator;

import org.junit.Test;

import static org.junit.Assert.*;


public class CalculatorUtilsUnitTest {
    @Test
    public void testConvertPaceToSpeed() {
        assertTrue("7,22".equals(CalculatorUtils.convertPaceToSpeed(Measurement.IMPERIAL, "5:10")));
        assertTrue("6,21".equals(CalculatorUtils.convertPaceToSpeed(Measurement.IMPERIAL, "6")));
        assertTrue("3,73".equals(CalculatorUtils.convertPaceToSpeed(Measurement.IMPERIAL, "10:00")));
        assertTrue("5,07".equals(CalculatorUtils.convertPaceToSpeed(Measurement.IMPERIAL, "07:21")));
        assertTrue("11,53".equals(CalculatorUtils.convertPaceToSpeed(Measurement.IMPERIAL, "03:14")));
        assertTrue("11,61".equals(CalculatorUtils.convertPaceToSpeed(Measurement.METRIC, "5:10")));
        assertTrue("10,00".equals(CalculatorUtils.convertPaceToSpeed(Measurement.METRIC, "6")));
        assertTrue("6,00".equals(CalculatorUtils.convertPaceToSpeed(Measurement.METRIC, "10:00")));
        assertTrue("8,16".equals(CalculatorUtils.convertPaceToSpeed(Measurement.METRIC, "07:21")));
        assertTrue("18,56".equals(CalculatorUtils.convertPaceToSpeed(Measurement.METRIC, "03:14")));
    }

    @Test
    public void testConvertPaceToSpeedExceptions() {
        int exceptionCounter = 0;

        try {
            CalculatorUtils.convertPaceToSpeed(Measurement.METRIC, "5:60");
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            CalculatorUtils.convertPaceToSpeed(Measurement.METRIC, "5:-1");
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            CalculatorUtils.convertPaceToSpeed(Measurement.METRIC, "5:");
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            CalculatorUtils.convertPaceToSpeed(Measurement.METRIC, "3f:-1");
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            CalculatorUtils.convertPaceToSpeed(Measurement.METRIC, "l-:01");
        } catch (Exception e) {
            exceptionCounter++;
        }

        try {
            CalculatorUtils.convertPaceToSpeed(Measurement.METRIC, "100:75");
        } catch (Exception e) {
            exceptionCounter++;
        }

        assertTrue(exceptionCounter == 6);
    }
}