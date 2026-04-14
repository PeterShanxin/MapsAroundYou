package mapsaroundyou.app;

import mapsaroundyou.model.SortMode;
import mapsaroundyou.model.TransportMode;
import mapsaroundyou.model.UserPreferences;

/**
 * Immutable form snapshot passed to {@link GuiSearchService#search(SearchRequest)}.
 *
 * @param destinationId selected destination identifier
 * @param maxRent maximum monthly rent in SGD
 * @param maxCommuteMinutes inclusive commute upper bound in minutes
 * @param maxTransfers inclusive cap on public-transport transfers
 * @param maxWalkMinutes inclusive cap on walking time in minutes
 * @param requireAircon when {@code true}, only air-conditioned listings are considered
 * @param transportMode commute mode used for matrix lookup
 * @param resultLimit maximum results returned
 * @param sortMode preferred ordering applied by {@link mapsaroundyou.service.ListingRanker}
 * @param excludeWalkDominantRoutes whether walk-dominant routes should be rejected
 */
public record SearchRequest(
        String destinationId,
        int maxRent,
        int maxCommuteMinutes,
        int maxTransfers,
        int maxWalkMinutes,
        boolean requireAircon,
        TransportMode transportMode,
        int resultLimit,
        SortMode sortMode,
        boolean excludeWalkDominantRoutes
) {
    /**
     * Converts this request into a {@link UserPreferences} snapshot for the domain logic layer.
     *
     * @return preferences equivalent to this request
     */
    public UserPreferences toUserPreferences() {
        return new UserPreferences(
                destinationId,
                maxRent,
                maxCommuteMinutes,
                maxTransfers,
                maxWalkMinutes,
                requireAircon,
                transportMode,
                resultLimit,
                sortMode,
                excludeWalkDominantRoutes
        );
    }
}
