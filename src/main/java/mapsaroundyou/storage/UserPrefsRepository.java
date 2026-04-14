package mapsaroundyou.storage;

import mapsaroundyou.model.UserPreferences;

/**
 * Persists last-used search preferences outside the bundled app data.
 */
public interface UserPrefsRepository {
    /**
     * Loads persisted preferences.
     *
     * @return preferences snapshot; implementations should return defaults when unavailable
     */
    UserPreferences load();

    /**
     * Persists the provided preferences snapshot.
     *
     * @param preferences preferences to store
     */
    void save(UserPreferences preferences);
}
