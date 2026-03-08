package mapsaroundyou.storage;

import mapsaroundyou.model.CommuteEstimate;

import java.util.Optional;
import java.util.Set;

public interface TravelTimeRepository {
    Optional<CommuteEstimate> findByOriginAndDestination(String originNodeId, String destinationId);

    Set<String> findKnownOrigins();

    Set<String> findKnownDestinations();
}
