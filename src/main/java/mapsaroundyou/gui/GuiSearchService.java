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

    public GuiSearchService(SearchLogic searchLogic) {
        this.searchLogic = Objects.requireNonNull(searchLogic, "searchLogic");
    }

    public List<Destination> getSupportedDestinations() {
        return guard("loading destinations", () -> searchLogic.getSupportedDestinations());
    }

    public DatasetMetadata getDatasetMetadata() {
        return guard("loading dataset metadata", () -> searchLogic.getDatasetMetadata());
    }

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

    public ListingDetails getListingDetails(String listingId) {
        return guard("loading listing details", () -> searchLogic.getListingDetails(listingId));
    }

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
