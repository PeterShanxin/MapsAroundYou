package mapsaroundyou.gui;

import mapsaroundyou.model.TransportMode;

/**
 * Immutable GUI form snapshot passed to {@link GuiSearchService#search(SearchRequest)}.
 *
 * @param destinationId selected destination identifier
 * @param maxRent maximum monthly rent in SGD
 * @param maxCommuteMinutes inclusive commute upper bound in minutes
 * @param requireAircon when {@code true}, only air-conditioned listings are considered
 * @param transportMode commute mode used for matrix lookup
 */
public record SearchRequest(
        String destinationId,
        int maxRent,
        int maxCommuteMinutes,
        boolean requireAircon,
        TransportMode transportMode
) {
}

