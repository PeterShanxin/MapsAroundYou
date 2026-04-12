package mapsaroundyou.common;

/**
 * Raised when a search completes successfully but yields no matching listings.
 */
public class NoResultsException extends MapsAroundYouException {
    private static final long serialVersionUID = 1L;

    public NoResultsException(String message) {
        super(message);
    }
}
