package mapsaroundyou.model;

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
        boolean requireAircon,
        TransportMode transportMode,
        int resultLimit,
        boolean excludeWalkDominantRoutes
) {
}
