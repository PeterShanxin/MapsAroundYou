package mapsaroundyou.model;

import java.util.Optional;

/**
 * Detailed listing view model used by later UI work.
 *
 * @param listing always-present listing payload
 * @param commuteEstimate populated when a destination is active for commute lookup
 */
public record ListingDetails(RentalListing listing, Optional<CommuteEstimate> commuteEstimate) {
}
