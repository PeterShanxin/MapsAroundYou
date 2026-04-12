package mapsaroundyou.common;

/**
 * Raised when bundled datasets cannot be loaded or validated. Concrete failures use
 * {@link DatasetIOException} or {@link DatasetIntegrityException}.
 */
public abstract class DataLoadException extends MapsAroundYouException {
    private static final long serialVersionUID = 1L;

    protected DataLoadException(String message) {
        super(message);
    }

    protected DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
