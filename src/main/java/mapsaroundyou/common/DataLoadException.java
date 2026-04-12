package mapsaroundyou.common;

/**
 * Raised when bundled datasets cannot be loaded or validated. Concrete failures use
 * {@link DatasetIOException} or {@link DatasetIntegrityException}.
 */
public abstract class DataLoadException extends MapsAroundYouException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message explanation of the dataset failure
     */
    protected DataLoadException(String message) {
        super(message);
    }

    /**
     * @param message explanation of the dataset failure
     * @param cause underlying I/O or parse error
     */
    protected DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
