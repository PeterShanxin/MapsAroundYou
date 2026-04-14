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
    /**
     * Returns a deterministic default preference snapshot.
     *
     * @return defaults used before any user input or persistence
     */
    public static UserPreferences defaults() {
        return AppConfig.defaultUserPreferences();
    }

    /**
     * Convenience constructor that uses the default transfer cap.
     *
     * @param destinationId selected destination id or {@code null}
     * @param maxRent inclusive rent ceiling in SGD
     * @param maxCommuteMinutes inclusive commute upper bound in minutes
     * @param maxWalkMinutes inclusive walking-time cap in minutes
     * @param requireAircon whether air-conditioning is mandatory
     * @param transportMode commute mode for matrix lookup
     * @param resultLimit maximum number of listings to return
     * @param sortMode requested ordering strategy
     * @param excludeWalkDominantRoutes when {@code true}, filters walk-heavy commutes
     */
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

    /**
     * Creates a copy with the destination replaced.
     *
     * @param updatedDestinationId new destination id (nullable)
     * @return updated preferences snapshot
     */
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
