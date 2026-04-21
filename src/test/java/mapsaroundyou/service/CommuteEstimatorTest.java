package mapsaroundyou.service;

import mapsaroundyou.common.DatasetIntegrityException;
import mapsaroundyou.common.InvalidInputException;
import mapsaroundyou.model.CommuteEstimate;
import mapsaroundyou.model.TransportMode;
import mapsaroundyou.storage.TravelTimeRepository;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommuteEstimatorTest {
    @Test
    void estimate_publicTransportPair_returnsStoredCommute() {
        CommuteEstimate expectedCommute = new CommuteEstimate("R01", "D01", 32, 21, 11, 1, 1.62d);
        CommuteEstimator estimator = new CommuteEstimator(new StubTravelTimeRepository(Map.of(
                "R01:D01", expectedCommute
        )));

        CommuteEstimate actualCommute = estimator.estimate("R01", "D01", TransportMode.PUBLIC_TRANSPORT);

        assertEquals(expectedCommute, actualCommute);
    }

    @Test
    void estimate_nullTransportMode_throwsInvalidInputException() {
        CommuteEstimator estimator = new CommuteEstimator(new StubTravelTimeRepository(Map.of()));

        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> estimator.estimate("R01", "D01", null)
        );

        assertEquals("Unsupported transport mode: null", exception.getMessage());
    }

    @Test
    void estimate_missingPair_throwsDatasetIntegrityException() {
        CommuteEstimator estimator = new CommuteEstimator(new StubTravelTimeRepository(Map.of()));

        DatasetIntegrityException exception = assertThrows(
                DatasetIntegrityException.class,
                () -> estimator.estimate("R01", "D01", TransportMode.PUBLIC_TRANSPORT)
        );

        assertEquals("No commute record for origin R01 and destination D01", exception.getMessage());
    }

    private static final class StubTravelTimeRepository implements TravelTimeRepository {
        private final Map<String, CommuteEstimate> commuteByPair;

        private StubTravelTimeRepository(Map<String, CommuteEstimate> commuteByPair) {
            this.commuteByPair = commuteByPair;
        }

        @Override
        public Optional<CommuteEstimate> findByOriginAndDestination(String originNodeId, String destinationId) {
            return Optional.ofNullable(commuteByPair.get(originNodeId + ":" + destinationId));
        }

        @Override
        public Set<String> findKnownOrigins() {
            return Set.of();
        }

        @Override
        public Set<String> findKnownDestinations() {
            return Set.of();
        }

        @Override
        public Map<String, Set<String>> findKnownDestinationsByOrigin() {
            return Map.of();
        }
    }
}
