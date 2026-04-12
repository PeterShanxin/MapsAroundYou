package mapsaroundyou.common;

/**
 * Raised when CLI, GUI, or logic input fails validation.
 */
public class InvalidInputException extends MapsAroundYouException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message validation guidance for the caller
     */
    public InvalidInputException(String message) {
        super(message);
    }

    /**
     * @param message validation guidance for the caller
     * @param cause root parse or constraint failure
     */
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
