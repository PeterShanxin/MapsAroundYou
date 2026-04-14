package mapsaroundyou.gui;

import mapsaroundyou.model.CommuteEstimate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GuiTextFormatterTest {
    @Test
    void formatCommute_returnsExpandedSingleLineSummary() {
        CommuteEstimate commute = new CommuteEstimate("R01", "D01", 40, 26, 14, 3, 1.72d);

        assertEquals(
                "40 min total (26 transit, 14 walk, 3 transfer(s), $1.72)",
                GuiTextFormatter.formatCommute(commute)
        );
    }

    @Test
    void formatMinutes_returnsMinuteLabel() {
        assertEquals("14 min", GuiTextFormatter.formatMinutes(14));
    }

    @Test
    void formatTransfers_returnsTransferLabel() {
        assertEquals("3 transfer(s)", GuiTextFormatter.formatTransfers(3));
    }

    @Test
    void formatFare_returnsCurrencyLabel() {
        assertEquals("$1.72", GuiTextFormatter.formatFare(1.72d));
    }
}
