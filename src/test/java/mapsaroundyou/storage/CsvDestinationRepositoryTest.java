package mapsaroundyou.storage;

import mapsaroundyou.common.DataLoadException;
import mapsaroundyou.model.Destination;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvDestinationRepositoryTest {
    @TempDir
    Path tempDir;

    @Test
    void findAll_validCsv_returnsDestinations() throws Exception {
        Path csvPath = tempDir.resolve("destinations.csv");
        Files.writeString(csvPath, """
                ID,Category,Location Name,Postal Code
                D01,University,NUS,117575
                D02,Hospital,NUH,119074
                """);

        CsvDestinationRepository repository = new CsvDestinationRepository(csvPath);

        List<Destination> destinations = repository.findAll();
        assertEquals(2, destinations.size());
        assertEquals("NUS", destinations.getFirst().name());
        assertEquals("119074", repository.findById("D02").orElseThrow().postalCode());
    }

    @Test
    void constructor_missingColumn_throwsDataLoadException() throws Exception {
        Path csvPath = tempDir.resolve("broken-destinations.csv");
        Files.writeString(csvPath, """
                ID,Category,Location Name
                D01,University,NUS
                """);

        DataLoadException exception = assertThrows(
                DataLoadException.class,
                () -> new CsvDestinationRepository(csvPath)
        );

        assertTrue(exception.getMessage().contains("Postal Code"));
    }

    @Test
    void constructor_duplicateDestinationId_throwsDataLoadException() throws Exception {
        Path csvPath = tempDir.resolve("duplicate-destinations.csv");
        Files.writeString(csvPath, """
                ID,Category,Location Name,Postal Code
                D01,University,NUS,117575
                D01,Hospital,NUH,119074
                """);

        DataLoadException exception = assertThrows(
                DataLoadException.class,
                () -> new CsvDestinationRepository(csvPath)
        );

        assertEquals("Duplicate destination id in " + csvPath + ": D01", exception.getMessage());
    }

    @Test
    void constructor_blankLocationName_throwsDataLoadException() throws Exception {
        Path csvPath = tempDir.resolve("blank-destinations.csv");
        Files.writeString(csvPath, """
                ID,Category,Location Name,Postal Code
                D01,University,   ,117575
                """);

        DataLoadException exception = assertThrows(
                DataLoadException.class,
                () -> new CsvDestinationRepository(csvPath)
        );

        assertTrue(exception.getMessage().contains("Blank value for 'Location Name'"));
    }
}
