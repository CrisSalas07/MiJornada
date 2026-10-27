package cr.ac.ucr.mijornada.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cr.ac.ucr.mijornada.model.ActivityValidator.ValidationResult;

/**
 * Unit tests for {@link ActivityValidator}, covering every error case and both range limits.
 */
public class ActivityValidatorTest {

    @Test
    public void emptyName_isRejected() {
        assertEquals(ValidationResult.EMPTY_NAME, ActivityValidator.validate("", "30"));
    }

    @Test
    public void spacesOnlyName_isRejected() {
        assertEquals(ValidationResult.EMPTY_NAME, ActivityValidator.validate("   ", "30"));
    }

    @Test
    public void emptyMinutes_isInvalid() {
        assertEquals(ValidationResult.INVALID_MINUTES, ActivityValidator.validate("Cálculo", ""));
    }

    @Test
    public void nonNumericMinutes_isInvalid() {
        assertEquals(ValidationResult.INVALID_MINUTES, ActivityValidator.validate("Cálculo", "abc"));
    }

    @Test
    public void zeroMinutes_isOutOfRange() {
        assertEquals(ValidationResult.OUT_OF_RANGE, ActivityValidator.validate("Cálculo", "0"));
    }

    @Test
    public void minutesAboveMax_isOutOfRange() {
        assertEquals(ValidationResult.OUT_OF_RANGE, ActivityValidator.validate("Cálculo", "481"));
    }

    @Test
    public void minimumMinutes_isAccepted() {
        assertEquals(ValidationResult.OK, ActivityValidator.validate("Cálculo", "1"));
    }

    @Test
    public void maximumMinutes_isAccepted() {
        assertEquals(ValidationResult.OK, ActivityValidator.validate("Cálculo", "480"));
    }

    @Test
    public void surroundingSpaces_areIgnored() {
        assertEquals(ValidationResult.OK, ActivityValidator.validate("  Cálculo  ", " 45 "));
    }
}
