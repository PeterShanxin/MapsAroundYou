package mapsaroundyou.gui;

import mapsaroundyou.app.SearchRequest;
import mapsaroundyou.common.AppConfig;
import mapsaroundyou.common.InvalidInputException;
import mapsaroundyou.model.Destination;
import mapsaroundyou.model.SortMode;
import mapsaroundyou.model.TransportMode;
import mapsaroundyou.model.UserPreferences;

import java.util.Objects;

/**
 * Converts raw UI input into validated {@link SearchRequest} values.
 */
public final class GuiSearchRequestParser {
    private GuiSearchRequestParser() {
    }

    public static SearchRequest parse(
            Destination destination,
            String maxRentRaw,
            String maxCommuteRaw,
            String maxWalkRaw,
            boolean requireAircon,
            String resultLimitRaw,
            SortMode sortMode,
            boolean excludeWalkDominantRoutes
    ) {
        return parse(
                destination,
                maxRentRaw,
                maxCommuteRaw,
                String.valueOf(AppConfig.DEFAULT_MAX_TRANSFERS),
                maxWalkRaw,
                requireAircon,
                resultLimitRaw,
                sortMode,
                excludeWalkDominantRoutes
        );
    }

    public static SearchRequest parse(
            Destination destination,
            String maxRentRaw,
            String maxCommuteRaw,
            String maxTransfersRaw,
            String maxWalkRaw,
            boolean requireAircon,
            String resultLimitRaw,
            SortMode sortMode,
            boolean excludeWalkDominantRoutes
    ) {
        Objects.requireNonNull(destination, "destination");
        if (sortMode == null) {
            throw new InvalidInputException("Sort mode is required.");
        }

        int maxCommuteMinutes = parseInt(maxCommuteRaw, "Max commute", 1);
        int maxWalkMinutes = parseInt(maxWalkRaw, "Max walk", 0);

        return new SearchRequest(
                destination.destinationId(),
                parseInt(maxRentRaw, "Max rent", 0),
                maxCommuteMinutes,
                parseInt(maxTransfersRaw, "Max transfers", 0),
                maxWalkMinutes,
                requireAircon,
                TransportMode.PUBLIC_TRANSPORT,
                parseInt(resultLimitRaw, "Result limit", 1),
                sortMode,
                excludeWalkDominantRoutes
        );
    }

    public static SearchRequest parse(
            Destination destination,
            String maxRentRaw,
            String maxCommuteRaw,
            boolean requireAircon
    ) {
        UserPreferences defaults = UserPreferences.defaults();
        return parse(
                destination,
                maxRentRaw,
                maxCommuteRaw,
                String.valueOf(defaults.maxTransfers()),
                String.valueOf(defaults.maxWalkMinutes()),
                requireAircon,
                String.valueOf(defaults.resultLimit()),
                defaults.sortMode(),
                defaults.excludeWalkDominantRoutes()
        );
    }

    private static int parseInt(String raw, String label, int minimumValue) {
        if (raw == null || raw.isBlank()) {
            throw new InvalidInputException(label + " is required.");
        }

        int value;
        try {
            value = Integer.parseInt(raw.trim());
        } catch (NumberFormatException exception) {
            throw new InvalidInputException(label + " must be a valid integer.");
        }

        if (value < minimumValue) {
            throw new InvalidInputException(label + " must be at least " + minimumValue + ".");
        }
        return value;
    }
}
