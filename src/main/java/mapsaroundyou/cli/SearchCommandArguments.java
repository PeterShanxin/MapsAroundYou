package mapsaroundyou.cli;

import mapsaroundyou.model.SortMode;

/**
 * Normalized search flags parsed from {@code search ...} argv tokens.
 *
 * @param destinationId destination identifier from the supported list
 * @param maxRent inclusive rent ceiling in SGD
 * @param maxCommuteMinutes inclusive commute upper bound in minutes
 * @param maxTransfers optional cap on public-transport transfers (null keeps the current preference)
 * @param maxWalkMinutes optional walking-time cap (null keeps the current preference)
 * @param requireAircon whether air-conditioning is mandatory
 * @param resultLimit optional maximum results to return (null keeps the current preference)
 * @param sortMode optional sort mode override (null keeps the current preference)
 * @param excludeWalkDominantRoutes whether walk-dominant routes should be rejected
 */
record SearchCommandArguments(
        String destinationId,
        int maxRent,
        int maxCommuteMinutes,
        Integer maxTransfers,
        Integer maxWalkMinutes,
        boolean requireAircon,
        Integer resultLimit,
        SortMode sortMode,
        boolean excludeWalkDominantRoutes
) {
}
