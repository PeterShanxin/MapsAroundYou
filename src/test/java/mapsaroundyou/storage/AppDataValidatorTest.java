package mapsaroundyou.storage;

import mapsaroundyou.common.DatasetIntegrityException;
import mapsaroundyou.model.CommuteEstimate;
import mapsaroundyou.model.Destination;
import mapsaroundyou.model.OriginNode;
import mapsaroundyou.model.RentalListing;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppDataValidatorTest {
    @Test
    void validate_consistentRepositories_passes() {
        assertDoesNotThrow(() -> AppDataValidator.validate(
                new StubOriginNodeRepository(List.of(
                        new OriginNode("R01", "111111", "West", "Clementi"),
                        new OriginNode("R02", "222222", "Central", "Bukit Timah")
                )),
                new StubDestinationRepository(List.of(
                        new Destination("D01", "NUS", "University", "", "117575"),
                        new Destination("D02", "NUH", "Hospital", "", "119074")
                )),
                new StubListingRepository(List.of(
                        new RentalListing("L001", "Listing A", 1500, true, "R01", "Addr 1", "HDB", "PG", ""),
                        new RentalListing("L002", "Listing B", 1600, false, "R02", "Addr 2", "HDB", "PG", "")
                )),
                new StubTravelTimeRepository(Map.of(
                        "R01:D01", new CommuteEstimate("R01", "D01", 30, 20, 10, 0, 1.50d),
                        "R01:D02", new CommuteEstimate("R01", "D02", 35, 23, 12, 1, 1.70d),
                        "R02:D01", new CommuteEstimate("R02", "D01", 28, 19, 9, 1, 1.45d),
                        "R02:D02", new CommuteEstimate("R02", "D02", 31, 22, 9, 0, 1.60d)
                ))
        ));
    }

    @Test
    void validate_listingWithUnknownOrigin_throwsDatasetIntegrityException() {
        DatasetIntegrityException exception = assertThrows(
                DatasetIntegrityException.class,
                () -> AppDataValidator.validate(
                        new StubOriginNodeRepository(List.of(new OriginNode("R01", "111111", "West", "Clementi"))),
                        defaultDestinationRepository(),
                        new StubListingRepository(List.of(
                                new RentalListing("L001", "Listing A", 1500, true, "R99", "Addr 1", "HDB", "PG", "")
                        )),
                        new StubTravelTimeRepository(Map.of(
                                "R99:D01", new CommuteEstimate("R99", "D01", 30, 20, 10, 0, 1.50d)
                        ))
                )
        );

        assertEquals("Listing L001 references unknown origin node R99", exception.getMessage());
    }

    @Test
    void validate_listingOriginWithoutTravelTimeRecords_throwsDatasetIntegrityException() {
        DatasetIntegrityException exception = assertThrows(
                DatasetIntegrityException.class,
                () -> AppDataValidator.validate(
                        new StubOriginNodeRepository(List.of(new OriginNode("R01", "111111", "West", "Clementi"))),
                        defaultDestinationRepository(),
                        new StubListingRepository(List.of(
                                new RentalListing("L001", "Listing A", 1500, true, "R01", "Addr 1", "HDB", "PG", "")
                        )),
                        new StubTravelTimeRepository(Map.of())
                )
        );

        assertEquals("Listing L001 has no travel-time records for origin R01", exception.getMessage());
    }

    @Test
    void validate_travelTimeWithUnknownOrigin_throwsDatasetIntegrityException() {
        DatasetIntegrityException exception = assertThrows(
                DatasetIntegrityException.class,
                () -> AppDataValidator.validate(
                        new StubOriginNodeRepository(List.of(new OriginNode("R01", "111111", "West", "Clementi"))),
                        defaultDestinationRepository(),
                        new StubListingRepository(List.of(
                                new RentalListing("L001", "Listing A", 1500, true, "R01", "Addr 1", "HDB", "PG", "")
                        )),
                        new StubTravelTimeRepository(Map.of(
                                "R01:D01", new CommuteEstimate("R01", "D01", 30, 20, 10, 0, 1.50d),
                                "R99:D01", new CommuteEstimate("R99", "D01", 35, 23, 12, 1, 1.70d)
                        ))
                )
        );

        assertEquals("Travel-time dataset references unknown origin node R99", exception.getMessage());
    }

    @Test
    void validate_travelTimeWithUnknownDestination_throwsDatasetIntegrityException() {
        DatasetIntegrityException exception = assertThrows(
                DatasetIntegrityException.class,
                () -> AppDataValidator.validate(
                        new StubOriginNodeRepository(List.of(new OriginNode("R01", "111111", "West", "Clementi"))),
                        defaultDestinationRepository(),
                        new StubListingRepository(List.of(
                                new RentalListing("L001", "Listing A", 1500, true, "R01", "Addr 1", "HDB", "PG", "")
                        )),
                        new StubTravelTimeRepository(Map.of(
                                "R01:D01", new CommuteEstimate("R01", "D01", 30, 20, 10, 0, 1.50d),
                                "R01:D99", new CommuteEstimate("R01", "D99", 35, 23, 12, 1, 1.70d)
                        ))
                )
        );

        assertEquals("Travel-time dataset references unknown destination D99", exception.getMessage());
    }

    @Test
    void validate_missingCoverageForDestination_throwsDatasetIntegrityException() {
        DatasetIntegrityException exception = assertThrows(
                DatasetIntegrityException.class,
                () -> AppDataValidator.validate(
                        new StubOriginNodeRepository(List.of(new OriginNode("R01", "111111", "West", "Clementi"))),
                        new StubDestinationRepository(List.of(
                                new Destination("D01", "NUS", "University", "", "117575"),
                                new Destination("D02", "NUH", "Hospital", "", "119074")
                        )),
                        new StubListingRepository(List.of(
                                new RentalListing("L001", "Listing A", 1500, true, "R01", "Addr 1", "HDB", "PG", "")
                        )),
                        new StubTravelTimeRepository(Map.of(
                                "R01:D01", new CommuteEstimate("R01", "D01", 30, 20, 10, 0, 1.50d)
                        ))
                )
        );

        assertEquals("Missing travel-time coverage for listing origin R01", exception.getMessage());
    }

    private static StubDestinationRepository defaultDestinationRepository() {
        return new StubDestinationRepository(List.of(
                new Destination("D01", "NUS", "University", "", "117575")
        ));
    }

    private record StubOriginNodeRepository(List<OriginNode> originNodes) implements OriginNodeRepository {
        @Override
        public List<OriginNode> findAll() {
            return originNodes;
        }

        @Override
        public Optional<OriginNode> findById(String originNodeId) {
            return originNodes.stream()
                    .filter(originNode -> originNode.originNodeId().equals(originNodeId))
                    .findFirst();
        }
    }

    private record StubDestinationRepository(List<Destination> destinations) implements DestinationRepository {
        @Override
        public List<Destination> findAll() {
            return destinations;
        }

        @Override
        public Optional<Destination> findById(String destinationId) {
            return destinations.stream()
                    .filter(destination -> destination.destinationId().equals(destinationId))
                    .findFirst();
        }
    }

    private record StubListingRepository(List<RentalListing> listings) implements ListingRepository {
        @Override
        public List<RentalListing> findAll() {
            return listings;
        }

        @Override
        public Optional<RentalListing> findById(String listingId) {
            return listings.stream().filter(listing -> listing.listingId().equals(listingId)).findFirst();
        }
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
            return commuteByPair.values().stream()
                    .map(CommuteEstimate::originNodeId)
                    .collect(java.util.stream.Collectors.toUnmodifiableSet());
        }

        @Override
        public Set<String> findKnownDestinations() {
            return commuteByPair.values().stream()
                    .map(CommuteEstimate::destinationId)
                    .collect(java.util.stream.Collectors.toUnmodifiableSet());
        }

        @Override
        public Map<String, Set<String>> findKnownDestinationsByOrigin() {
            Map<String, Set<String>> destinationsByOrigin = new java.util.LinkedHashMap<>();
            for (CommuteEstimate commuteEstimate : commuteByPair.values()) {
                destinationsByOrigin.computeIfAbsent(
                                commuteEstimate.originNodeId(),
                                ignored -> new java.util.LinkedHashSet<>())
                        .add(commuteEstimate.destinationId());
            }
            return destinationsByOrigin.entrySet().stream()
                    .collect(java.util.stream.Collectors.toUnmodifiableMap(
                            Map.Entry::getKey,
                            entry -> Set.copyOf(entry.getValue())
                    ));
        }
    }
}
