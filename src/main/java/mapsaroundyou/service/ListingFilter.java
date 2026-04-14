package mapsaroundyou.service;

import mapsaroundyou.model.RentalListing;

import java.util.List;

/**
 * Applies listing-level filters before commute lookup.
 */
public class ListingFilter {
    /**
     * Retains listings at or below {@code maxRent}.
     *
     * @param listings candidate listings
     * @param maxRent inclusive rent ceiling in SGD
     * @return filtered, immutable list
     */
    public List<RentalListing> filterByRent(List<RentalListing> listings, int maxRent) {
        return listings.stream()
                .filter(listing -> listing.monthlyRent() <= maxRent)
                .toList();
    }

    /**
     * Optionally requires air-conditioning.
     *
     * @param listings candidate listings
     * @param requireAircon when {@code true}, drops listings without air-conditioning
     * @return filtered or copied listings
     */
    public List<RentalListing> filterByAircon(List<RentalListing> listings, boolean requireAircon) {
        if (!requireAircon) {
            return List.copyOf(listings);
        }
        return listings.stream()
                .filter(RentalListing::hasAircon)
                .toList();
    }
}
