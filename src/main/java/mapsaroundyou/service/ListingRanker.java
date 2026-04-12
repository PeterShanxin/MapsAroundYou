package mapsaroundyou.service;

import mapsaroundyou.model.SearchResult;

import java.util.Comparator;
import java.util.List;

/**
 * Produces deterministic ranking for shortlisted results.
 */
public class ListingRanker {
    private static final Comparator<SearchResult> DEFAULT_COMPARATOR = Comparator
            .comparingInt((SearchResult result) -> result.commute().totalMinutes())
            .thenComparingInt(result -> result.listing().monthlyRent())
            .thenComparing(result -> result.listing().listingId());

    /**
     * Sorts results by commute time, rent, then listing id for deterministic output.
     *
     * @param results scored candidates
     * @return sorted immutable list
     */
    public List<SearchResult> rank(List<SearchResult> results) {
        return results.stream()
                .sorted(DEFAULT_COMPARATOR)
                .toList();
    }

    /**
     * Computes a bounded heuristic score using normalized commute and rent versus caller caps.
     *
     * @param result listing and commute to score
     * @param maxRent rent normalization baseline (should be positive)
     * @param maxCommuteMinutes commute normalization baseline (should be positive)
     * @return score in the range {@code [0.0, 1.0]}
     */
    public double computeScore(SearchResult result, int maxRent, int maxCommuteMinutes) {
        double normalizedCommute = (double) result.commute().totalMinutes() / maxCommuteMinutes;
        double normalizedRent = (double) result.listing().monthlyRent() / maxRent;
        double rawScore = 1.0d - ((0.6d * normalizedCommute) + (0.4d * normalizedRent));
        return Math.max(0.0d, rawScore);
    }
}
