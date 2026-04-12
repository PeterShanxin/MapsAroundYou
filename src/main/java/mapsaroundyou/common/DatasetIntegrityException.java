package mapsaroundyou.common;

/**
 * Raised when bundled data is missing, inconsistent, or does not match the expected schema.
 */
public final class DatasetIntegrityException extends DataLoadException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message schema, duplication, or referential integrity details
     */
    public DatasetIntegrityException(String message) {
        super(message);
    }

    /**
     * @param message schema, duplication, or referential integrity details
     * @param cause parse or validation cause
     */
    public DatasetIntegrityException(String message, Throwable cause) {
        super(message, cause);
    }
}
