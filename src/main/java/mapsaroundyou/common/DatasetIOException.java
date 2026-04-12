package mapsaroundyou.common;

/**
 * Raised when a bundled dataset cannot be read due to an I/O failure (including close errors).
 */
public final class DatasetIOException extends DataLoadException {
    private static final long serialVersionUID = 1L;

    public DatasetIOException(String message) {
        super(message);
    }

    public DatasetIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
