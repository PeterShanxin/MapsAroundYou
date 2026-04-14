package mapsaroundyou.model;

import java.util.Locale;

/**
 * Supported shortlist sorting strategies.
 */
public enum SortMode {
    COMMUTE("Commute", "commute"),
    RENT("Rent", "rent"),
    BALANCED("Balanced", "balanced");

    private final String displayName;
    private final String cliValue;

    SortMode(String displayName, String cliValue) {
        this.displayName = displayName;
        this.cliValue = cliValue;
    }

    /**
     * Returns the CLI token for this sort mode.
     *
     * @return CLI value (lowercase)
     */
    public String cliValue() {
        return cliValue;
    }

    /**
     * Parses a CLI token into a {@link SortMode}.
     *
     * @param rawValue user-supplied token
     * @return matching sort mode
     * @throws IllegalArgumentException when unknown
     */
    public static SortMode fromCliValue(String rawValue) {
        String normalized = rawValue == null ? "" : rawValue.trim().toLowerCase(Locale.ROOT);
        for (SortMode sortMode : values()) {
            if (sortMode.cliValue.equals(normalized)) {
                return sortMode;
            }
        }
        throw new IllegalArgumentException("Unknown sort mode: " + rawValue);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
