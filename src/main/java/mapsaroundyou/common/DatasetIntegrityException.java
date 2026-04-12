package mapsaroundyou.common;

/**
 * Raised when bundled data is missing, inconsistent, or does not match the expected schema.
 */
public final class DatasetIntegrityException extends DataLoadException {
    private static final long serialVersionUID = 1L;

    public DatasetIntegrityException(String message) {
        super(message);
    }

    public DatasetIntegrityException(String message, Throwable cause) {
        super(message, cause);
    }
}
