package mapsaroundyou.common;

/**
 * Raised when a listing id is not present in the local dataset.
 */
public class ListingNotFoundException extends MapsAroundYouException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message explanation referencing the missing listing id
     */
    public ListingNotFoundException(String message) {
        super(message);
    }
}
