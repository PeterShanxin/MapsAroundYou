package mapsaroundyou.gui;

import mapsaroundyou.model.CommuteEstimate;
import mapsaroundyou.model.RentalListing;
import mapsaroundyou.model.SearchResult;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

class SearchRowTest {
    @Test
    void getters_returnUiFriendlyListingAndCommuteValues() {
        SearchResult result = new SearchResult(
                new RentalListing("L001", "Listing A", 1500, false, "R01", "Addr 1", "HDB", "PG", "Note"),
                new CommuteEstimate("R01", "D01", 42, 28, 14, 2, 1.85d),
                0.42d
        );

        SearchRow row = new SearchRow(result);

        assertEquals("L001", row.getListingId());
        assertEquals("Listing A", row.getTitle());
        assertEquals(1500, row.getMonthlyRent());
        assertFalse(row.hasAircon());
        assertEquals(42, row.getTotalCommuteMinutes());
        assertEquals(14, row.getWalkMinutes());
        assertEquals(2, row.getTransfers());
        assertEquals(0.42d, row.getScore());
        assertSame(result.commute(), row.commute());
        assertSame(result.listing(), row.listing());
    }
}
