package mapsaroundyou.gui;

import mapsaroundyou.model.CommuteEstimate;
import mapsaroundyou.model.DatasetMetadata;
import mapsaroundyou.model.Destination;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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

    @Test
    void formatDestination_includesCategoryAndAreaWhenPresent() {
        Destination destination = new Destination("D01", "NUS", "University", "Kent Ridge", "117575");

        assertEquals("NUS (University • Kent Ridge)", GuiTextFormatter.formatDestination(destination));
    }

    @Test
    void formatDatasetMetadata_includesDateAndSource() {
        DatasetMetadata metadata = new DatasetMetadata(LocalDate.of(2026, 4, 20), "Curated offline dataset");

        assertEquals(
                "Dataset last updated: 2026-04-20 • Curated offline dataset",
                GuiTextFormatter.formatDatasetMetadata(metadata)
        );
    }

    @Test
    void formatOptionalText_blankValue_returnsDash() {
        assertEquals("-", GuiTextFormatter.formatOptionalText("   "));
    }
}
