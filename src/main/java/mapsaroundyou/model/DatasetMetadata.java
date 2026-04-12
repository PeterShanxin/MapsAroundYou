package mapsaroundyou.model;

import java.time.LocalDate;

/**
 * Dataset freshness metadata for later UI display.
 *
 * @param lastUpdated calendar date describing when the bundle was refreshed
 * @param sourceDescription human-readable provenance blurb (may be empty)
 */
public record DatasetMetadata(LocalDate lastUpdated, String sourceDescription) {
}
