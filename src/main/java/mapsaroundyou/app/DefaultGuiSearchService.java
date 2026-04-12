package mapsaroundyou.app;

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
 * Default {@link GuiSearchService} that delegates to {@link SearchLogic} and maps expected
 * exceptions for the UI.
 */
public final class DefaultGuiSearchService implements GuiSearchService {
    private static final Logger LOGGER = Logger.getLogger(DefaultGuiSearchService.class.getName());

    private final SearchLogic searchLogic;

    /**
     * @param searchLogic domain workflow used for all GUI operations
     */
    public DefaultGuiSearchService(SearchLogic searchLogic) {
        this.searchLogic = Objects.requireNonNull(searchLogic, "searchLogic");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Destination> getSupportedDestinations() {
        return guard("loading destinations", () -> searchLogic.getSupportedDestinations());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatasetMetadata getDatasetMetadata() {
        return guard("loading dataset metadata", () -> searchLogic.getDatasetMetadata());
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
