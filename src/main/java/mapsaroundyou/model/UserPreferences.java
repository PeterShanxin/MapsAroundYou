package mapsaroundyou.model;

import mapsaroundyou.common.AppConfig;

/**
 * Current search preferences.
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
