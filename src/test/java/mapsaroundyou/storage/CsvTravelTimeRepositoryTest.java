package mapsaroundyou.storage;

import mapsaroundyou.common.DataLoadException;
import mapsaroundyou.model.CommuteEstimate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvTravelTimeRepositoryTest {
    @TempDir
    Path tempDir;

    @Test
    void findByOriginAndDestination_validCsv_readsTransfers() throws Exception {
        Path csvPath = tempDir.resolve("transit_matrix.csv");
        Files.writeString(csvPath, """
                flat_id,destination_id,pt_total,pt_walk,pt_transit,pt_transfers,pt_fare
                R01,D01,28,13,15,1,1.59
                """);

        CsvTravelTimeRepository repository = new CsvTravelTimeRepository(csvPath);
        Optional<CommuteEstimate> commute = repository.findByOriginAndDestination("R01", "D01");

        assertTrue(commute.isPresent());
        assertEquals(1, commute.get().transfers());
        assertEquals(28, commute.get().totalMinutes());
    }

    @Test
    void constructor_missingTransfersColumn_throwsDataLoadException() throws Exception {
        Path csvPath = tempDir.resolve("broken-transit.csv");
        Files.writeString(csvPath, """
                flat_id,destination_id,pt_total,pt_walk,pt_transit,pt_fare
                R01,D01,28,13,15,1.59
                """);

        DataLoadException exception = assertThrows(
                DataLoadException.class,
                () -> new CsvTravelTimeRepository(csvPath)
        );

        assertTrue(exception.getMessage().contains("pt_transfers"));
    }

    @Test
    void findKnownOriginsAndDestinations_validCsv_returnsCoverageViews() throws Exception {
        Path csvPath = tempDir.resolve("coverage-transit.csv");
        Files.writeString(csvPath, """
                flat_id,destination_id,pt_total,pt_walk,pt_transit,pt_transfers,pt_fare
                R01,D01,28,13,15,1,1.59
                R01,D02,31,11,20,0,1.72
                R02,D01,24,10,14,2,1.40
                """);

        CsvTravelTimeRepository repository = new CsvTravelTimeRepository(csvPath);

        assertEquals(Set.of("R01", "R02"), repository.findKnownOrigins());
        assertEquals(Set.of("D01", "D02"), repository.findKnownDestinations());
        assertEquals(Map.of(
                "R01", Set.of("D01", "D02"),
                "R02", Set.of("D01")
        ), repository.findKnownDestinationsByOrigin());
    }

    @Test
    void constructor_negativeTransfers_throwsDataLoadException() throws Exception {
        Path csvPath = tempDir.resolve("negative-transfers.csv");
        Files.writeString(csvPath, """
                flat_id,destination_id,pt_total,pt_walk,pt_transit,pt_transfers,pt_fare
                R01,D01,28,13,15,-1,1.59
                """);

        DataLoadException exception = assertThrows(
                DataLoadException.class,
                () -> new CsvTravelTimeRepository(csvPath)
        );

        assertTrue(exception.getMessage().contains("must be non-negative"));
    }

    @Test
    void constructor_duplicatePair_throwsDataLoadException() throws Exception {
        Path csvPath = tempDir.resolve("duplicate-pair.csv");
        Files.writeString(csvPath, """
                flat_id,destination_id,pt_total,pt_walk,pt_transit,pt_transfers,pt_fare
                R01,D01,28,13,15,1,1.59
                R01,D01,30,12,18,2,1.72
                """);

        DataLoadException exception = assertThrows(
                DataLoadException.class,
                () -> new CsvTravelTimeRepository(csvPath)
        );

        assertEquals("Duplicate travel-time pair in " + csvPath + ": R01 -> D01", exception.getMessage());
    }
}
