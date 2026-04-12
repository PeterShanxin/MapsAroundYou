package mapsaroundyou.gui;

import mapsaroundyou.model.DatasetMetadata;
import mapsaroundyou.model.SearchResult;

import java.util.List;

/**
 * Search outcome containing provenance metadata and an immutable ranked listing list.
 *
 * @param datasetMetadata metadata returned alongside the search
 * @param results ranked matches (defensively copied)
 */
public record SearchResponse(
        DatasetMetadata datasetMetadata,
        List<SearchResult> results
) {
    /**
     * Normalizes {@code results} to an unmodifiable copy for safe FX threading handoff.
     *
     * @param datasetMetadata see {@link SearchResponse#datasetMetadata()}
     * @param results mutable or immutable result list
     */
    public SearchResponse {
        results = List.copyOf(results);
    }
}

