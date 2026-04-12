package mapsaroundyou.model;

/**
 * Commute estimate derived from the offline transit matrix.
 *
 * @param originNodeId matrix origin key
 * @param destinationId matrix destination key
 * @param totalMinutes door-to-door minutes for public transport
 * @param transitMinutes in-vehicle or in-system transit minutes
 * @param walkMinutes access, egress, and transfer walking minutes
 * @param transfers number of transfers required
 * @param fare estimated cash fare in SGD from the dataset
 */
public record CommuteEstimate(
        String originNodeId,
        String destinationId,
        int totalMinutes,
        int transitMinutes,
        int walkMinutes,
        int transfers,
        double fare
) {
}
