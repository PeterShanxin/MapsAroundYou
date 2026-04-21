package mapsaroundyou.storage;

import mapsaroundyou.common.DataLoadException;
import mapsaroundyou.model.DatasetMetadata;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PropertiesDatasetMetadataRepositoryTest {
    @Test
    void load_validResource_returnsDatasetMetadata() {
        PropertiesDatasetMetadataRepository repository = new PropertiesDatasetMetadataRepository(
                "mapsaroundyou/storage/dataset-metadata-valid.properties"
        );

        DatasetMetadata metadata = repository.load();

        assertEquals(LocalDate.of(2026, 4, 20), metadata.lastUpdated());
        assertEquals("Curated offline dataset", metadata.sourceDescription());
    }

    @Test
    void constructor_missingLastUpdated_throwsDataLoadException() {
        DataLoadException exception = assertThrows(
                DataLoadException.class,
                () -> new PropertiesDatasetMetadataRepository(
                        "mapsaroundyou/storage/dataset-metadata-missing-last-updated.properties"
                )
        );

        assertEquals(
                "Missing lastUpdated in dataset metadata: "
                        + "mapsaroundyou/storage/dataset-metadata-missing-last-updated.properties",
                exception.getMessage()
        );
    }

    @Test
    void constructor_invalidDate_throwsDataLoadException() {
        DataLoadException exception = assertThrows(
                DataLoadException.class,
                () -> new PropertiesDatasetMetadataRepository(
                        "mapsaroundyou/storage/dataset-metadata-invalid-date.properties"
                )
        );

        assertEquals("Invalid lastUpdated value in dataset metadata: 2026-13-40", exception.getMessage());
    }

    @Test
    void constructor_missingResource_throwsDataLoadException() {
        DataLoadException exception = assertThrows(
                DataLoadException.class,
                () -> new PropertiesDatasetMetadataRepository("mapsaroundyou/storage/missing.properties")
        );

        assertEquals("Missing resource: mapsaroundyou/storage/missing.properties", exception.getMessage());
    }
}
