package mapsaroundyou.common;

/**
 * Raised when a destination id is not part of the supported dataset.
 */
public class DestinationNotFoundException extends MapsAroundYouException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message explanation referencing the unknown destination id
     */
    public DestinationNotFoundException(String message) {
        super(message);
    }
}
