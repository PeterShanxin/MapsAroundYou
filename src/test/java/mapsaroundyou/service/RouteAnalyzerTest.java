package mapsaroundyou.service;

import mapsaroundyou.model.CommuteEstimate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouteAnalyzerTest {
    @Test
    void isWalkDominant_ratioAtThreshold_returnsTrue() {
        RouteAnalyzer routeAnalyzer = new RouteAnalyzer(0.60d);

        boolean isWalkDominant = routeAnalyzer.isWalkDominant(
                new CommuteEstimate("R01", "D01", 30, 12, 18, 1, 1.50d)
        );

        assertTrue(isWalkDominant);
    }

    @Test
    void isWalkDominant_ratioBelowThreshold_returnsFalse() {
        RouteAnalyzer routeAnalyzer = new RouteAnalyzer(0.60d);

        boolean isWalkDominant = routeAnalyzer.isWalkDominant(
                new CommuteEstimate("R01", "D01", 30, 13, 17, 1, 1.50d)
        );

        assertFalse(isWalkDominant);
    }

    @Test
    void isWalkDominant_zeroMinuteCommute_returnsFalse() {
        RouteAnalyzer routeAnalyzer = new RouteAnalyzer(0.60d);

        boolean isWalkDominant = routeAnalyzer.isWalkDominant(
                new CommuteEstimate("R01", "D01", 0, 0, 0, 0, 0.00d)
        );

        assertFalse(isWalkDominant);
    }

    @Test
    void summarize_returnsHumanReadableBreakdown() {
        RouteAnalyzer routeAnalyzer = new RouteAnalyzer(0.60d);

        String summary = routeAnalyzer.summarize(
                new CommuteEstimate("R01", "D01", 42, 28, 14, 2, 1.72d)
        );

        assertEquals("Total 42 min (28 min transit, 14 min walk, fare SGD 1.72)", summary);
    }
}
