package mapsaroundyou.storage;

import mapsaroundyou.common.DatasetIntegrityException;
import mapsaroundyou.common.DatasetIOException;
import mapsaroundyou.model.OriginNode;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * {@link OriginNodeRepository} backed by a UTF-8 CSV with a header row.
 */
public final class CsvOriginNodeRepository implements OriginNodeRepository {
    private static final String[] REQUIRED_HEADERS = {"Flat_ID", "Postal_Code", "Region", "Area_Name"};

    private final Map<String, OriginNode> originNodesById;

    /**
     * Loads origin nodes from a classpath resource.
     *
     * @param resourcePath resource path relative to the class loader
     * @throws DatasetIOException if the file cannot be read or closed
     * @throws DatasetIntegrityException if the CSV is empty, duplicated, or malformed
     */
    public CsvOriginNodeRepository(String resourcePath) {
        this(CsvSupport.classpathReader(resourcePath), resourcePath);
    }

    /**
     * Loads origin nodes from an absolute filesystem path.
     *
     * @param filePath CSV location on disk
     * @throws DatasetIOException if the file cannot be read or closed
     * @throws DatasetIntegrityException if the CSV is empty, duplicated, or malformed
     */
    public CsvOriginNodeRepository(Path filePath) {
        this(CsvSupport.fileReader(filePath), filePath.toString());
    }

    private CsvOriginNodeRepository(ReaderSupplier readerSupplier, String sourceName) {
        this.originNodesById = load(readerSupplier, sourceName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OriginNode> findAll() {
        return List.copyOf(originNodesById.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<OriginNode> findById(String originNodeId) {
        return Optional.ofNullable(originNodesById.get(originNodeId));
    }

    private static Map<String, OriginNode> load(ReaderSupplier readerSupplier, String sourceName) {
        Map<String, OriginNode> originNodes = new LinkedHashMap<>();
        try (CSVParser parser = CsvSupport.openParser(readerSupplier, sourceName, REQUIRED_HEADERS)) {
            for (CSVRecord record : parser) {
                String originNodeId = CsvSupport.requireValue(record, "Flat_ID", sourceName);
                if (originNodes.containsKey(originNodeId)) {
                    throw new DatasetIntegrityException(
                            "Duplicate origin node id in " + sourceName + ": " + originNodeId);
                }

                OriginNode originNode = new OriginNode(
                        originNodeId,
                        CsvSupport.requireValue(record, "Postal_Code", sourceName),
                        CsvSupport.requireValue(record, "Region", sourceName),
                        CsvSupport.requireValue(record, "Area_Name", sourceName)
                );
                originNodes.put(originNodeId, originNode);
            }
        } catch (java.io.IOException exception) {
            throw new DatasetIOException("Failed to close dataset: " + sourceName, exception);
        }
        if (originNodes.isEmpty()) {
            throw new DatasetIntegrityException("No origin nodes found in " + sourceName);
        }
        return originNodes;
    }
}
