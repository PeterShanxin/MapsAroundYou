package mapsaroundyou.storage;

import mapsaroundyou.model.RentalListing;

import java.util.List;
import java.util.Optional;

/**
 * Read-only access to rental listings.
 */
public interface ListingRepository {
    /**
     * Returns every listing in the catalog.
     *
     * @return immutable or copied collection
     */
    List<RentalListing> findAll();

    /**
     * Looks up a listing by id.
     *
     * @param listingId candidate id
     * @return the listing when present
     */
    Optional<RentalListing> findById(String listingId);
}
