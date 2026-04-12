package mapsaroundyou.storage;

import mapsaroundyou.model.DatasetMetadata;

/**
 * Loads dataset provenance metadata from application resources.
 */
public interface DatasetMetadataRepository {
    /**
     * Returns parsed metadata describing freshness and sources.
     *
     * @return cached metadata instance
     * @throws mapsaroundyou.common.DatasetIOException if the resource cannot be read
     * @throws mapsaroundyou.common.DatasetIntegrityException if required fields are invalid
     */
    DatasetMetadata load();
}
