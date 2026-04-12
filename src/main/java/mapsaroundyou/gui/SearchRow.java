package mapsaroundyou.gui;

import mapsaroundyou.model.CommuteEstimate;
import mapsaroundyou.model.RentalListing;
import mapsaroundyou.model.SearchResult;

import java.util.Objects;

/**
 * UI-friendly view of {@link SearchResult} for TableView display.
 */
public final class SearchRow {
    private final SearchResult result;

    /**
     * @param result backing domain result for this table row
     */
    public SearchRow(SearchResult result) {
        this.result = Objects.requireNonNull(result, "result");
    }

    /**
     * @return the underlying {@link SearchResult}
     */
    public SearchResult result() {
        return result;
    }

    /**
     * @return listing id suitable for detail lookups
     */
    public String getListingId() {
        return listing().listingId();
    }

    /**
     * @return listing title for display
     */
    public String getTitle() {
        return listing().title();
    }

    /**
     * @return monthly rent in SGD
     */
    public int getMonthlyRent() {
        return listing().monthlyRent();
    }

    /**
     * @return {@code true} when the listing advertises air-conditioning
     */
    public boolean isHasAircon() {
        return listing().hasAircon();
    }

    /**
     * @return total commute minutes for the active destination
     */
    public int getTotalCommuteMinutes() {
        return commute().totalMinutes();
    }

    /**
     * @return ranking score computed by {@link mapsaroundyou.service.ListingRanker}
     */
    public double getScore() {
        return result.score();
    }

    /**
     * @return commute breakdown for the listing
     */
    public CommuteEstimate commute() {
        return result.commute();
    }

    /**
     * @return rental listing payload
     */
    public RentalListing listing() {
        return result.listing();
    }
}

