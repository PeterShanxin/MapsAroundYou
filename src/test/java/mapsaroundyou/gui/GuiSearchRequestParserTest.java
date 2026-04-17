package mapsaroundyou.gui;

import mapsaroundyou.app.SearchRequest;
import mapsaroundyou.common.AppConfig;
import mapsaroundyou.common.InvalidInputException;
import mapsaroundyou.model.Destination;
import mapsaroundyou.model.SortMode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GuiSearchRequestParserTest {
    private static final Destination DESTINATION =
            new Destination("D01", "NUS", "University", "Kent Ridge", "117575");

    @Test
    void parse_validValues_returnsSearchRequest() {
        SearchRequest request = GuiSearchRequestParser.parse(
                DESTINATION,
                "2200",
                "45",
                "1",
                "10",
                true,
                "5",
                SortMode.BALANCED,
                true
        );

        assertEquals("D01", request.destinationId());
        assertEquals(2200, request.maxRent());
        assertEquals(45, request.maxCommuteMinutes());
        assertEquals(1, request.maxTransfers());
        assertEquals(10, request.maxWalkMinutes());
        assertEquals(true, request.requireAircon());
        assertEquals(5, request.resultLimit());
        assertEquals(SortMode.BALANCED, request.sortMode());
        assertEquals(true, request.excludeWalkDominantRoutes());
    }

    @Test
    void parse_walkPreferenceDisabled_keepsIndependentWalkLimit() {
        SearchRequest request = GuiSearchRequestParser.parse(
                DESTINATION,
                "2200",
                "45",
                "2",
                "12",
                false,
                "5",
                SortMode.BALANCED,
                false
        );

        assertEquals(12, request.maxWalkMinutes());
        assertEquals(2, request.maxTransfers());
        assertEquals(false, request.excludeWalkDominantRoutes());
    }

    @Test
    void parse_negativeRent_throwsInvalidInputException() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> GuiSearchRequestParser.parse(DESTINATION, "-1", "45", "1", "10", false, "5",
                        SortMode.COMMUTE, false)
        );

        assertEquals("Max rent must be at least 0.", exception.getMessage());
    }

    @Test
    void parse_zeroCommute_throwsInvalidInputException() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> GuiSearchRequestParser.parse(DESTINATION, "2000", "0", "1", "10", false, "5",
                        SortMode.COMMUTE, false)
        );

        assertEquals("Max commute must be at least 1.", exception.getMessage());
    }

    @Test
    void parse_zeroResultLimit_throwsInvalidInputException() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> GuiSearchRequestParser.parse(DESTINATION, "2000", "45", "1", "10", false, "0",
                        SortMode.COMMUTE, false)
        );

        assertEquals("Result limit must be at least 1.", exception.getMessage());
    }

    @Test
    void parse_negativeTransfers_throwsInvalidInputException() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> GuiSearchRequestParser.parse(DESTINATION, "2000", "45", "-1", "10", false, "5",
                        SortMode.COMMUTE, false)
        );

        assertEquals("Max transfers must be at least 0.", exception.getMessage());
    }

    @Test
    void parse_blankTransfers_usesUnlimitedDefault() {
        SearchRequest request = GuiSearchRequestParser.parse(
                DESTINATION,
                "2200",
                "45",
                "",
                "10",
                false,
                "5",
                SortMode.BALANCED,
                false
        );

        assertEquals(AppConfig.DEFAULT_MAX_TRANSFERS, request.maxTransfers());
    }
}
