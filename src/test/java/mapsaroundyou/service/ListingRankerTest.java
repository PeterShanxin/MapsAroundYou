package mapsaroundyou.service;

import mapsaroundyou.model.CommuteEstimate;
import mapsaroundyou.model.RentalListing;
import mapsaroundyou.model.SearchResult;
import mapsaroundyou.model.SortMode;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListingRankerTest {
    private final ListingRanker listingRanker = new ListingRanker();

    @Test
    void rank_commuteSort_usesCommuteRentThenListingId() {
        List<SearchResult> rankedResults = listingRanker.rank(List.of(
                searchResult("L003", 1800, 30, 0.40d),
                searchResult("L001", 1600, 25, 0.20d),
                searchResult("L002", 1400, 25, 0.90d)
        ), SortMode.COMMUTE);

        assertEquals(List.of("L002", "L001", "L003"),
                rankedResults.stream().map(result -> result.listing().listingId()).toList());
    }

    @Test
    void rank_rentSort_usesRentCommuteThenListingId() {
        List<SearchResult> rankedResults = listingRanker.rank(List.of(
                searchResult("L003", 1400, 35, 0.90d),
                searchResult("L002", 1400, 30, 0.20d),
                searchResult("L001", 1600, 20, 0.80d)
        ), SortMode.RENT);

        assertEquals(List.of("L002", "L003", "L001"),
                rankedResults.stream().map(result -> result.listing().listingId()).toList());
    }

    @Test
    void rank_balancedSort_usesScoreDescendingThenTieBreakers() {
        List<SearchResult> rankedResults = listingRanker.rank(List.of(
                searchResult("L003", 1500, 32, 0.95d),
                searchResult("L001", 1500, 25, 0.80d),
                searchResult("L002", 1400, 25, 0.80d)
        ), SortMode.BALANCED);

        assertEquals(List.of("L003", "L002", "L001"),
                rankedResults.stream().map(result -> result.listing().listingId()).toList());
    }

    @Test
    void computeScore_normalizesCommuteAndRentWithEqualWeights() {
        SearchResult result = searchResult("L001", 1500, 30, 0.00d);

        double score = listingRanker.computeScore(result, 2000, 60);

        assertEquals(0.375d, score, 1.0e-9);
    }

    @Test
    void computeScore_neverReturnsNegativeValues() {
        SearchResult result = searchResult("L001", 5000, 120, 0.00d);

        double score = listingRanker.computeScore(result, 2000, 60);

        assertEquals(0.0d, score, 1.0e-9);
    }

    private static SearchResult searchResult(String listingId, int monthlyRent, int totalMinutes, double score) {
        return new SearchResult(
                new RentalListing(
                        listingId,
                        "Listing " + listingId,
                        monthlyRent,
                        true,
                        "R" + listingId,
                        "Addr " + listingId,
                        "HDB",
                        "PropertyGuru",
                        ""
                ),
                new CommuteEstimate("R" + listingId, "D01", totalMinutes, totalMinutes - 5, 5, 1, 1.50d),
                score
        );
    }
}
