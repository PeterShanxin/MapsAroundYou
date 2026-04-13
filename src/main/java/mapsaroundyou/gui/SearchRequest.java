package mapsaroundyou.gui;

import mapsaroundyou.common.AppConfig;
import mapsaroundyou.model.SortMode;
import mapsaroundyou.model.TransportMode;
import mapsaroundyou.model.UserPreferences;

public record SearchRequest(
        String destinationId,
        int maxRent,
        int maxCommuteMinutes,
        int maxTransfers,
        int maxWalkMinutes,
        boolean requireAircon,
        TransportMode transportMode,
        int resultLimit,
        SortMode sortMode,
        boolean excludeWalkDominantRoutes
) {
    public SearchRequest(
            String destinationId,
            int maxRent,
            int maxCommuteMinutes,
            int maxWalkMinutes,
            boolean requireAircon,
            TransportMode transportMode,
            int resultLimit,
            SortMode sortMode,
            boolean excludeWalkDominantRoutes
    ) {
        this(
                destinationId,
                maxRent,
                maxCommuteMinutes,
                AppConfig.DEFAULT_MAX_TRANSFERS,
                maxWalkMinutes,
                requireAircon,
                transportMode,
                resultLimit,
                sortMode,
                excludeWalkDominantRoutes
        );
    }

    public UserPreferences toUserPreferences() {
        return new UserPreferences(
                destinationId,
                maxRent,
                maxCommuteMinutes,
                maxTransfers,
                maxWalkMinutes,
                requireAircon,
                transportMode,
                resultLimit,
                sortMode,
                excludeWalkDominantRoutes
        );
    }
}

