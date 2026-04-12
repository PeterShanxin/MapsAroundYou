package mapsaroundyou.storage;

import mapsaroundyou.model.CommuteEstimate;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Read-only access to the offline public-transit commute matrix.
 */
public interface TravelTimeRepository {
    /**
     * Looks up a commute estimate for an origin/destination pair.
     *
     * @param originNodeId matrix row key
     * @param destinationId matrix column key
     * @return estimate when the pair exists
     */
    Optional<CommuteEstimate> findByOriginAndDestination(String originNodeId, String destinationId);

    /**
     * Returns every origin id present in the matrix.
     *
     * @return unmodifiable origin key set
     */
    Set<String> findKnownOrigins();

    /**
     * Returns every destination id referenced by any row.
     *
     * @return unmodifiable destination key set
     */
    Set<String> findKnownDestinations();

    /**
     * Maps each origin to the destination ids it can reach.
     *
     * @return unmodifiable map of unmodifiable destination sets
     */
    Map<String, Set<String>> findKnownDestinationsByOrigin();
}
