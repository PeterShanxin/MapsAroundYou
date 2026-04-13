package mapsaroundyou.model;

import mapsaroundyou.common.AppConfig;

/**
 * Current search preferences.
 *
 * @param destinationId selected destination id or {@code null} before configuration
 * @param maxRent inclusive rent ceiling in SGD
 * @param maxCommuteMinutes inclusive commute upper bound in minutes
 * @param requireAircon whether air-conditioning is mandatory
 * @param transportMode commute mode for matrix lookup
 * @param resultLimit maximum number of listings to return
 * @param excludeWalkDominantRoutes when {@code true}, filters walk-heavy commutes
 */
public record UserPreferences(
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
    public static UserPreferences defaults() {
        return AppConfig.defaultUserPreferences();
    }

    public UserPreferences(
            String destinationId,
            int maxRent,
            int maxCommuteMinutes,
            int maxWalkMinutes,
            boolean requireAircon,
            TransportMode transportMode,
            int resultLimit,
            SortMode sortMode,
            boolean excludeWalkDominantRoutes
    ) {
        this(
                destinationId,
                maxRent,
                maxCommuteMinutes,
                AppConfig.DEFAULT_MAX_TRANSFERS,
                maxWalkMinutes,
                requireAircon,
                transportMode,
                resultLimit,
                sortMode,
                excludeWalkDominantRoutes
        );
    }

    public UserPreferences withDestination(String updatedDestinationId) {
        return new UserPreferences(
                updatedDestinationId,
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
