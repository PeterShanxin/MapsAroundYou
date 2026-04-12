package mapsaroundyou.logic;

import mapsaroundyou.model.CommuteEstimate;
import mapsaroundyou.model.DatasetMetadata;
import mapsaroundyou.model.Destination;
import mapsaroundyou.model.ListingDetails;
import mapsaroundyou.model.SearchResult;
import mapsaroundyou.model.TransportMode;
import mapsaroundyou.model.UserPreferences;

import java.util.List;

/**
 * Stateful search orchestration API shared by the CLI and GUI. Callers must set a destination and
 * preferences before generating a shortlist.
 */
public interface SearchLogic {
    /**
     * Returns all destinations supported by the bundled dataset.
     *
     * @return destinations in stable repository order
     * @throws mapsaroundyou.common.DatasetIOException if a dataset cannot be read
     * @throws mapsaroundyou.common.DatasetIntegrityException if bundled data fails validation
     */
    List<Destination> getSupportedDestinations();

    /**
     * Returns metadata describing dataset freshness and provenance.
     *
     * @return metadata loaded from application resources
     * @throws mapsaroundyou.common.DatasetIOException if metadata cannot be read
     * @throws mapsaroundyou.common.DatasetIntegrityException if metadata values are invalid
     */
    DatasetMetadata getDatasetMetadata();

    /**
     * Sets the active destination used for commute lookups and ranking.
     *
     * @param destinationId non-blank destination identifier
     * @throws mapsaroundyou.common.InvalidInputException if {@code destinationId} is null or blank
     * @throws mapsaroundyou.common.DestinationNotFoundException if the id is unknown
     */
    void setDestination(String destinationId);

    /**
     * Updates affordability, commute cap, air-conditioning, and transport mode preferences.
     *
     * @param maxRent maximum monthly rent in SGD (non-negative)
     * @param maxCommuteMinutes inclusive upper bound on commute time in minutes (at least 1)
     * @param requireAircon when {@code true}, only listings with air-conditioning are considered
     * @param transportMode commute mode to use for matrix lookup
     * @throws mapsaroundyou.common.InvalidInputException if inputs are out of range or null mode
     */
    void setPreferences(int maxRent, int maxCommuteMinutes, boolean requireAircon, TransportMode transportMode);

    /**
     * Filters, ranks, and returns the top shortlist for the current preferences.
     *
     * @return ranked results limited by the configured cap
     * @throws mapsaroundyou.common.InvalidInputException if destination or preferences are unset
     * @throws mapsaroundyou.common.NoResultsException if no listing matches the filters
     * @throws mapsaroundyou.common.DestinationNotFoundException if the destination id is invalid
     * @throws mapsaroundyou.common.DatasetIntegrityException if commute data is missing for a pair
     */
    List<SearchResult> generateShortlist();

    /**
     * Loads listing details, optionally including a commute estimate when a destination is set.
     *
     * @param listingId non-blank listing identifier
     * @return listing and optional commute for the current destination
     * @throws mapsaroundyou.common.InvalidInputException if {@code listingId} is null or blank
     * @throws mapsaroundyou.common.ListingNotFoundException if the listing is absent
     * @throws mapsaroundyou.common.DatasetIntegrityException if commute data is missing
     */
    ListingDetails getListingDetails(String listingId);

    /**
     * Returns the commute estimate between a listing origin and the active destination.
     *
     * @param listingId non-blank listing identifier
     * @return commute breakdown for the current preferences
     * @throws mapsaroundyou.common.InvalidInputException if prerequisites are missing or id is blank
     * @throws mapsaroundyou.common.ListingNotFoundException if the listing is absent
     * @throws mapsaroundyou.common.DatasetIntegrityException if commute data is missing
     */
    CommuteEstimate getCommuteDetails(String listingId);

    /**
     * Returns the latest preference snapshot (including defaults before configuration).
     *
     * @return current preferences; not null
     */
    UserPreferences getCurrentPreferences();
}
