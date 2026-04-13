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

    /**
     * Parses UI fields into a validated {@link SearchRequest}, using the default transfer cap.
     *
     * @param destination selected destination
     * @param maxRentRaw max rent field value
     * @param maxCommuteRaw max commute field value
     * @param maxWalkRaw max walk field value
     * @param requireAircon whether air-conditioning is required
     * @param resultLimitRaw result limit field value
     * @param sortMode chosen sort mode
     * @param excludeWalkDominantRoutes whether walk-dominant routes are rejected
     * @return validated request snapshot
     */
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

    /**
     * Parses UI fields into a validated {@link SearchRequest}.
     *
     * @param destination selected destination
     * @param maxRentRaw max rent field value
     * @param maxCommuteRaw max commute field value
     * @param maxTransfersRaw max transfers field value
     * @param maxWalkRaw max walk field value
     * @param requireAircon whether air-conditioning is required
     * @param resultLimitRaw result limit field value
     * @param sortMode chosen sort mode
     * @param excludeWalkDominantRoutes whether walk-dominant routes are rejected
     * @return validated request snapshot
     */
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

    /**
     * Parses a minimal set of UI inputs into a request, using deterministic defaults for
     * unspecified fields.
     *
     * @param destination selected destination
     * @param maxRentRaw max rent field value
     * @param maxCommuteRaw max commute field value
     * @param requireAircon whether air-conditioning is required
     * @return validated request snapshot
     */
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
