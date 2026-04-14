package mapsaroundyou.storage;

import mapsaroundyou.common.DatasetIntegrityException;
import mapsaroundyou.common.DatasetIOException;
import mapsaroundyou.model.DatasetMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Properties;

/**
 * {@link DatasetMetadataRepository} that reads a Java {@link Properties} resource at construction time.
 */
public final class PropertiesDatasetMetadataRepository implements DatasetMetadataRepository {
    private final DatasetMetadata datasetMetadata;

    /**
     * Parses metadata from a classpath properties file.
     *
     * @param resourcePath resource path relative to the class loader
     * @throws DatasetIOException if the resource cannot be read
     * @throws DatasetIntegrityException if required keys or date formats are invalid
     */
    public PropertiesDatasetMetadataRepository(String resourcePath) {
        this.datasetMetadata = load(resourcePath);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatasetMetadata load() {
        return datasetMetadata;
    }

    private static DatasetMetadata load(String resourcePath) {
        Properties properties = new Properties();
        try (InputStream inputStream = PropertiesDatasetMetadataRepository.class.getClassLoader()
                .getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new DatasetIntegrityException("Missing resource: " + resourcePath);
            }
            properties.load(inputStream);
        } catch (IOException exception) {
            throw new DatasetIOException("Failed to read dataset metadata: " + resourcePath, exception);
        }

        String lastUpdated = properties.getProperty("lastUpdated");
        String sourceDescription = properties.getProperty("sourceDescription", "");
        if (lastUpdated == null || lastUpdated.isBlank()) {
            throw new DatasetIntegrityException("Missing lastUpdated in dataset metadata: " + resourcePath);
        }

        try {
            return new DatasetMetadata(LocalDate.parse(lastUpdated.trim()), sourceDescription.trim());
        } catch (DateTimeException exception) {
            throw new DatasetIntegrityException("Invalid lastUpdated value in dataset metadata: " + lastUpdated,
                    exception);
        }
    }
}
