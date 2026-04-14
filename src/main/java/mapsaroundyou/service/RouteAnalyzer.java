package mapsaroundyou.service;

import mapsaroundyou.model.CommuteEstimate;

/**
 * Placeholder for V1.4 route sanity checks and summaries.
 */
public class RouteAnalyzer {
    private final double walkDominantThreshold;

    /**
     * @param walkDominantThreshold inclusive ratio of walk minutes to total minutes treated as
     *                              walk-dominant
     */
    public RouteAnalyzer(double walkDominantThreshold) {
        this.walkDominantThreshold = walkDominantThreshold;
    }

    /**
     * Determines whether walking constitutes most of the commute duration.
     *
     * @param commuteEstimate commute under test
     * @return {@code true} when the walk ratio meets the configured threshold
     */
    public boolean isWalkDominant(CommuteEstimate commuteEstimate) {
        if (commuteEstimate.totalMinutes() == 0) {
            return false;
        }
        return ((double) commuteEstimate.walkMinutes() / commuteEstimate.totalMinutes()) >= walkDominantThreshold;
    }

    /**
     * Builds a short human-readable summary of a commute.
     *
     * @param commuteEstimate commute to describe
     * @return formatted summary string
     */
    public String summarize(CommuteEstimate commuteEstimate) {
        return String.format(
                "Total %d min (%d min transit, %d min walk, fare SGD %.2f)",
                commuteEstimate.totalMinutes(),
                commuteEstimate.transitMinutes(),
                commuteEstimate.walkMinutes(),
                commuteEstimate.fare()
        );
    }
}
