package mapsaroundyou.storage;

import mapsaroundyou.common.DataLoadException;
import mapsaroundyou.model.CommuteEstimate;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CsvTravelTimeRepository implements TravelTimeRepository {
    private static final String[] REQUIRED_HEADERS = {
            "flat_id",
            "destination_id",
            "pt_total",
            "pt_walk",
            "pt_transit",
            "pt_fare"
    };

    private final Map<String, Map<String, CommuteEstimate>> commuteByOriginThenDestination;

    public CsvTravelTimeRepository(String resourcePath) {
        this(CsvSupport.classpathReader(resourcePath), resourcePath);
    }

    public CsvTravelTimeRepository(Path filePath) {
        this(CsvSupport.fileReader(filePath), filePath.toString());
    }

    private CsvTravelTimeRepository(ReaderSupplier readerSupplier, String sourceName) {
        this.commuteByOriginThenDestination = load(readerSupplier, sourceName);
    }

    @Override
    public Optional<CommuteEstimate> findByOriginAndDestination(String originNodeId, String destinationId) {
        return Optional.ofNullable(commuteByOriginThenDestination
                .getOrDefault(originNodeId, Map.of())
                .get(destinationId));
    }

    @Override
    public Set<String> findKnownOrigins() {
        return Set.copyOf(commuteByOriginThenDestination.keySet());
    }

    @Override
    public Set<String> findKnownDestinations() {
        return commuteByOriginThenDestination.values().stream()
                .flatMap(destinationMap -> destinationMap.keySet().stream())
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }

    private static Map<String, Map<String, CommuteEstimate>> load(ReaderSupplier readerSupplier, String sourceName) {
        Map<String, Map<String, CommuteEstimate>> commutes = new LinkedHashMap<>();
        try (CSVParser parser = CsvSupport.openParser(readerSupplier, sourceName, REQUIRED_HEADERS)) {
            for (CSVRecord record : parser) {
                String originNodeId = CsvSupport.requireValue(record, "flat_id", sourceName);
                String destinationId = CsvSupport.requireValue(record, "destination_id", sourceName);

                Map<String, CommuteEstimate> byDestination =
                        commutes.computeIfAbsent(originNodeId, ignored -> new LinkedHashMap<>());
                if (byDestination.containsKey(destinationId)) {
                    throw new DataLoadException("Duplicate travel-time pair in " + sourceName + ": "
                            + originNodeId + " -> " + destinationId);
                }

                CommuteEstimate commuteEstimate = new CommuteEstimate(
                        originNodeId,
                        destinationId,
                        CsvSupport.parseRequiredInt(record, "pt_total", sourceName),
                        CsvSupport.parseRequiredInt(record, "pt_transit", sourceName),
                        CsvSupport.parseRequiredInt(record, "pt_walk", sourceName),
                        0,
                        CsvSupport.parseRequiredDouble(record, "pt_fare", sourceName)
                );
                byDestination.put(destinationId, commuteEstimate);
            }
        } catch (java.io.IOException exception) {
            throw new DataLoadException("Failed to close dataset: " + sourceName, exception);
        }
        if (commutes.isEmpty()) {
            throw new DataLoadException("No travel-time records found in " + sourceName);
        }
        return commutes;
    }
}
