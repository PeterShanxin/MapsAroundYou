package mapsaroundyou.service;

import mapsaroundyou.common.DatasetIntegrityException;
import mapsaroundyou.common.InvalidInputException;
import mapsaroundyou.model.CommuteEstimate;
import mapsaroundyou.model.TransportMode;
import mapsaroundyou.storage.TravelTimeRepository;

/**
 * Looks up commute estimates from the local travel-time matrix.
 */
public class CommuteEstimator {
    private final TravelTimeRepository travelTimeRepository;

    /**
     * @param travelTimeRepository matrix lookup backend
     */
    public CommuteEstimator(TravelTimeRepository travelTimeRepository) {
        this.travelTimeRepository = travelTimeRepository;
    }

    /**
     * Looks up a public-transit commute between an origin node and destination.
     *
     * @param originNodeId matrix row key
     * @param destinationId matrix column key
     * @param transportMode currently only {@link TransportMode#PUBLIC_TRANSPORT} is supported
     * @return commute breakdown
     * @throws InvalidInputException if the mode is unsupported
     * @throws DatasetIntegrityException if the matrix lacks the requested pair
     */
    public CommuteEstimate estimate(String originNodeId, String destinationId, TransportMode transportMode) {
        if (transportMode != TransportMode.PUBLIC_TRANSPORT) {
            throw new InvalidInputException("Unsupported transport mode: " + transportMode);
        }
        return travelTimeRepository.findByOriginAndDestination(originNodeId, destinationId)
                .orElseThrow(() -> new DatasetIntegrityException(
                        "No commute record for origin " + originNodeId + " and destination " + destinationId));
    }
}
