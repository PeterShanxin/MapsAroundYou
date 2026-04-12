package mapsaroundyou.common;

/**
 * Base type for application-specific failures surfaced to users or CLI/GUI layers.
 */
public abstract class MapsAroundYouException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected MapsAroundYouException(String message) {
        super(message);
    }

    protected MapsAroundYouException(String message, Throwable cause) {
        super(message, cause);
    }
}
