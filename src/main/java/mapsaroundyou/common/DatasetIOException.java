package mapsaroundyou.common;

/**
 * Raised when a bundled dataset cannot be read due to an I/O failure (including close errors).
 */
public final class DatasetIOException extends DataLoadException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message explanation of the read or close failure
     */
    public DatasetIOException(String message) {
        super(message);
    }

    /**
     * @param message explanation of the read or close failure
     * @param cause underlying I/O problem
     */
    public DatasetIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
