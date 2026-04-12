package mapsaroundyou.model;

/**
 * Curated demo listing used by the CLI scaffold.
 *
 * @param listingId stable identifier for UI and repository lookups
 * @param title marketing title from the source platform
 * @param monthlyRent rent in SGD per month
 * @param hasAircon whether air-conditioning is advertised
 * @param originNodeId travel-matrix origin key shared with {@link mapsaroundyou.model.OriginNode}
 * @param address free-form address text
 * @param roomType bedroom or layout description
 * @param sourcePlatform originating marketplace name
 * @param notes optional curator notes (may be blank)
 */
public record RentalListing(
        String listingId,
        String title,
        int monthlyRent,
        boolean hasAircon,
        String originNodeId,
        String address,
        String roomType,
        String sourcePlatform,
        String notes
) {
}
