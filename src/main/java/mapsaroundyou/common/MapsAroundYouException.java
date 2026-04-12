package mapsaroundyou.common;

/**
 * Base type for application-specific failures surfaced to users or CLI/GUI layers.
 */
public abstract class MapsAroundYouException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message user-safe explanation
     */
    protected MapsAroundYouException(String message) {
        super(message);
    }

    /**
     * @param message user-safe explanation
     * @param cause underlying failure
     */
    protected MapsAroundYouException(String message, Throwable cause) {
        super(message, cause);
    }
}
