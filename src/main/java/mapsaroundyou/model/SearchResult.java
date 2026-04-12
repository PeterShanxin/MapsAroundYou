package mapsaroundyou.model;

/**
 * Ranked listing result returned by the logic layer.
 *
 * @param listing rental metadata shown in the UI
 * @param commute public-transit estimate for the active destination
 * @param score heuristic ranking score (higher is better)
 */
public record SearchResult(RentalListing listing, CommuteEstimate commute, double score) {
}
