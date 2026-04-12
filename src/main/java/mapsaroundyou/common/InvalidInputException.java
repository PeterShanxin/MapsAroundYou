package mapsaroundyou.common;

/**
 * Raised when CLI, GUI, or logic input fails validation.
 */
public class InvalidInputException extends MapsAroundYouException {
    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
