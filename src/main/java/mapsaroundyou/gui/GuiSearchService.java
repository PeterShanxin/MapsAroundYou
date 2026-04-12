package mapsaroundyou.gui;

import mapsaroundyou.common.MapsAroundYouException;
import mapsaroundyou.logic.SearchLogic;
import mapsaroundyou.model.DatasetMetadata;
import mapsaroundyou.model.Destination;
import mapsaroundyou.model.ListingDetails;
import mapsaroundyou.model.SearchResult;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thin GUI-facing facade that hides the stateful {@link SearchLogic} call order.
 * Surfaces {@link MapsAroundYouException} subtypes to the UI; unexpected failures are logged and rethrown with cause.
 */
public final class GuiSearchService {
    private static final Logger LOGGER = Logger.getLogger(GuiSearchService.class.getName());

    private final SearchLogic searchLogic;

    /**
     * @param searchLogic domain workflow used for all GUI operations
     */
    public GuiSearchService(SearchLogic searchLogic) {
        this.searchLogic = Objects.requireNonNull(searchLogic, "searchLogic");
    }

    /**
     * Loads supported destinations for combo-box population.
     *
     * @return destinations from the repository
     * @throws MapsAroundYouException for expected domain failures
     * @throws IllegalStateException if an unexpected runtime error occurs
     */
    public List<Destination> getSupportedDestinations() {
        return guard("loading destinations", () -> searchLogic.getSupportedDestinations());
    }

    /**
     * Loads dataset provenance metadata for display.
     *
     * @return metadata describing freshness and sources
     * @throws MapsAroundYouException for expected domain failures
     * @throws IllegalStateException if an unexpected runtime error occurs
     */
    public DatasetMetadata getDatasetMetadata() {
        return guard("loading dataset metadata", () -> searchLogic.getDatasetMetadata());
    }

    /**
     * Applies {@code request} to {@link SearchLogic} and returns ranked results with metadata.
     *
     * @param request non-null search parameters from the form
     * @return immutable snapshot of metadata and results
     * @throws MapsAroundYouException for expected domain failures
     * @throws IllegalStateException if an unexpected runtime error occurs
     */
    public SearchResponse search(SearchRequest request) {
        Objects.requireNonNull(request, "request");
        return guard("search", () -> {
            searchLogic.setDestination(request.destinationId());
            searchLogic.setPreferences(
                    request.maxRent(),
                    request.maxCommuteMinutes(),
                    request.requireAircon(),
                    request.transportMode()
            );
            List<SearchResult> results = searchLogic.generateShortlist();
            return new SearchResponse(searchLogic.getDatasetMetadata(), results);
        });
    }

    /**
     * Loads listing details for the details pane.
     *
     * @param listingId listing identifier from the results table
     * @return listing and optional commute estimate
     * @throws MapsAroundYouException for expected domain failures
     * @throws IllegalStateException if an unexpected runtime error occurs
     */
    public ListingDetails getListingDetails(String listingId) {
        return guard("loading listing details", () -> searchLogic.getListingDetails(listingId));
    }

    /**
     * Runs {@code supplier} while preserving {@link MapsAroundYouException} and logging unexpected
     * runtime failures.
     *
     * @param operation short description for log messages
     * @param supplier delegated call
     * @param <T> result type
     * @return supplier result
     * @throws MapsAroundYouException rethrown as-is
     * @throws IllegalStateException wrapping unexpected runtime failures
     */
    private <T> T guard(String operation, Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (MapsAroundYouException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            LOGGER.log(Level.SEVERE, "Unexpected failure while " + operation, exception);
            throw new IllegalStateException("Unexpected failure while " + operation + ".", exception);
        }
    }
}
