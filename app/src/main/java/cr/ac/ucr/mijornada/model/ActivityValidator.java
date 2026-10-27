package cr.ac.ucr.mijornada.model;

/**
 * Validates the raw text typed by the user before an {@link ActividadEstudio} is created.
 * Kept free of Android classes so the rules can be unit tested on the JVM.
 */
public final class ActivityValidator {

    /** Smallest accepted duration, in minutes. */
    public static final int MIN_MINUTES = 1;

    /** Largest accepted duration, in minutes (8 hours). */
    public static final int MAX_MINUTES = 480;

    /** Possible outcomes of a validation, so the UI can show a specific message for each problem. */
    public enum ValidationResult {
        /** Both fields are valid. */
        OK,
        /** The name is empty or only spaces. */
        EMPTY_NAME,
        /** The minutes are empty or not a whole number. */
        INVALID_MINUTES,
        /** The minutes are a number outside {@link #MIN_MINUTES}..{@link #MAX_MINUTES}. */
        OUT_OF_RANGE
    }

    private ActivityValidator() {
        // Utility class: only static methods.
    }

    /**
     * Checks the name first and then the minutes, returning the first problem found.
     *
     * @param name        raw name text; surrounding spaces are ignored
     * @param minutesText raw minutes text; surrounding spaces are ignored
     * @return {@link ValidationResult#OK} or the first error found
     */
    public static ValidationResult validate(String name, String minutesText) {
        if (name == null || name.trim().isEmpty()) {
            return ValidationResult.EMPTY_NAME;
        }
        if (minutesText == null) {
            return ValidationResult.INVALID_MINUTES;
        }
        int minutes;
        try {
            minutes = Integer.parseInt(minutesText.trim());
        } catch (NumberFormatException e) {
            // Also covers the empty string and numbers too large for an int.
            return ValidationResult.INVALID_MINUTES;
        }
        if (minutes < MIN_MINUTES || minutes > MAX_MINUTES) {
            return ValidationResult.OUT_OF_RANGE;
        }
        return ValidationResult.OK;
    }
}
