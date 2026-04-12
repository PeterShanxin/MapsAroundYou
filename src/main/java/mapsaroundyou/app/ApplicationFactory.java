package mapsaroundyou.app;

import mapsaroundyou.common.AppConfig;
import mapsaroundyou.logic.DefaultSearchLogic;
import mapsaroundyou.logic.SearchLogic;
import mapsaroundyou.service.CommuteEstimator;
import mapsaroundyou.service.ListingFilter;
import mapsaroundyou.service.ListingRanker;
import mapsaroundyou.service.RouteAnalyzer;
import mapsaroundyou.storage.AppDataValidator;
import mapsaroundyou.storage.CsvDestinationRepository;
import mapsaroundyou.storage.CsvListingRepository;
import mapsaroundyou.storage.CsvOriginNodeRepository;
import mapsaroundyou.storage.CsvTravelTimeRepository;
import mapsaroundyou.storage.PropertiesDatasetMetadataRepository;

/**
 * Single composition root: constructs concrete {@link mapsaroundyou.storage} adapters and domain
 * services, then exposes entry points for CLI ({@link SearchLogic}) and GUI
 * ({@link GuiSearchService}).
 */
public final class ApplicationFactory {
    private ApplicationFactory() {
    }

    /**
     * Wires {@link DefaultGuiSearchService} over {@link #createSearchLogic()} for JavaFX.
     *
     * @return GUI-facing application service (narrower than raw {@link SearchLogic})
     */
    public static GuiSearchService createGuiSearchService() {
        return new DefaultGuiSearchService(createSearchLogic());
    }

    /**
     * Loads bundled CSV repositories, validates cross-dataset integrity, and returns a wired
     * {@link SearchLogic} instance.
     *
     * @return configured search logic
     * @throws mapsaroundyou.common.DatasetIOException if a dataset cannot be read
     * @throws mapsaroundyou.common.DatasetIntegrityException if validation fails
     */
    public static SearchLogic createSearchLogic() {
        CsvDestinationRepository destinationRepository =
                new CsvDestinationRepository(AppConfig.DESTINATIONS_RESOURCE);
        CsvOriginNodeRepository originNodeRepository =
                new CsvOriginNodeRepository(AppConfig.ORIGIN_NODES_RESOURCE);
        CsvListingRepository listingRepository =
                new CsvListingRepository(AppConfig.LISTINGS_RESOURCE);
        CsvTravelTimeRepository travelTimeRepository =
                new CsvTravelTimeRepository(AppConfig.TRAVEL_TIMES_RESOURCE);
        PropertiesDatasetMetadataRepository datasetMetadataRepository =
                new PropertiesDatasetMetadataRepository(AppConfig.DATASET_METADATA_RESOURCE);

        AppDataValidator.validate(
                originNodeRepository,
                destinationRepository,
                listingRepository,
                travelTimeRepository
        );

        return new DefaultSearchLogic(
                destinationRepository,
                listingRepository,
                datasetMetadataRepository,
                new ListingFilter(),
                new CommuteEstimator(travelTimeRepository),
                new ListingRanker(),
                new RouteAnalyzer(AppConfig.DEFAULT_WALK_DOMINANT_THRESHOLD)
        );
    }
}

