package mapsaroundyou.storage;

import mapsaroundyou.model.Destination;

import java.util.List;
import java.util.Optional;

/**
 * Read-only access to supported destinations.
 */
public interface DestinationRepository {
    /**
     * Returns every known destination.
     *
     * @return immutable or copied collection
     */
    List<Destination> findAll();

    /**
     * Looks up a destination by identifier.
     *
     * @param destinationId candidate id
     * @return the destination when present
     */
    Optional<Destination> findById(String destinationId);
}
