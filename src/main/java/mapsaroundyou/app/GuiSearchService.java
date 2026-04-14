package mapsaroundyou.app;

import mapsaroundyou.model.DatasetMetadata;
import mapsaroundyou.model.Destination;
import mapsaroundyou.model.ListingDetails;
import mapsaroundyou.model.UserPreferences;

import java.util.List;

/**
 * Application-layer search API for the JavaFX UI. Hides stateful {@link mapsaroundyou.logic.SearchLogic}
 * usage and error-shaping from controllers.
 */
public interface GuiSearchService {
    /**
     * Loads supported destinations for combo-box population.
     *
     * @return destinations from the backing repository
     */
    List<Destination> getSupportedDestinations();

    /**
     * Loads dataset provenance metadata for display.
     *
     * @return metadata describing freshness and sources
     */
    DatasetMetadata getDatasetMetadata();

    /**
     * Runs a search from a form snapshot and returns ranked results with metadata.
     *
     * @param request non-null search parameters from the UI
     * @return immutable snapshot of metadata and results
     */
    SearchResponse search(SearchRequest request);

    /**
     * Resolves listing details for the details pane.
     *
     * @param listingId listing identifier from the results table
     * @return listing and optional commute estimate
     */
    ListingDetails getListingDetails(String listingId);

    /**
     * Loads the latest preference snapshot (including persisted values when available).
     *
     * @return preferences used to prefill the UI
     */
    UserPreferences getCurrentPreferences();
}
