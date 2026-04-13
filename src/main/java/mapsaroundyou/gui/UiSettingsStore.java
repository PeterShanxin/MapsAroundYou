package mapsaroundyou.gui;

import mapsaroundyou.model.PersonaPreset;

import java.util.Objects;
import java.util.prefs.Preferences;

/**
 * Persists a small amount of UI state (persona preset + dark mode) for onboarding/UX.
 *
 * <p>All values are treated as best-effort; missing/invalid values fall back to
 * deterministic defaults. If the backing {@link Preferences} implementation
 * throws at runtime (for example {@link SecurityException} or
 * {@link IllegalStateException}), reads fall back to those defaults and writes
 * are skipped so the GUI is not disrupted.</p>
 */
public final class UiSettingsStore {
    private static final String KEY_PERSONA_PRESET = "personaPreset";
    private static final String KEY_DARK_MODE_ENABLED = "darkModeEnabled";

    private final Preferences preferences;

    /**
     * Creates a settings store backed by the current user's preference node.
     */
    public UiSettingsStore() {
        this(Preferences.userNodeForPackage(UiSettingsStore.class));
    }

    UiSettingsStore(Preferences preferences) {
        this.preferences = Objects.requireNonNull(preferences, "preferences");
    }

    /**
     * Loads the last-selected persona preset.
     *
     * @return stored preset, or {@link PersonaPreset#NEW_USER} when unset/invalid
     */
    public PersonaPreset loadPersonaPreset() {
        String stored;
        try {
            stored = preferences.get(KEY_PERSONA_PRESET, null);
        } catch (RuntimeException unused) {
            return PersonaPreset.NEW_USER;
        }
        if (stored == null) {
            return PersonaPreset.NEW_USER;
        }

        try {
            return PersonaPreset.valueOf(stored);
        } catch (IllegalArgumentException exception) {
            return PersonaPreset.NEW_USER;
        }
    }

    /**
     * Persists the selected persona preset.
     *
     * @param preset preset to store
     */
    public void savePersonaPreset(PersonaPreset preset) {
        Objects.requireNonNull(preset, "preset");
        try {
            preferences.put(KEY_PERSONA_PRESET, preset.name());
        } catch (RuntimeException unused) {
            // Best-effort persistence only.
        }
    }

    /**
     * Returns whether dark mode is enabled.
     *
     * @return {@code true} if enabled
     */
    public boolean isDarkModeEnabled() {
        try {
            return preferences.getBoolean(KEY_DARK_MODE_ENABLED, false);
        } catch (RuntimeException unused) {
            return false;
        }
    }

    /**
     * Persists the dark mode toggle value.
     *
     * @param enabled whether dark mode is enabled
     */
    public void setDarkModeEnabled(boolean enabled) {
        try {
            preferences.putBoolean(KEY_DARK_MODE_ENABLED, enabled);
        } catch (RuntimeException unused) {
            // Best-effort persistence only.
        }
    }
}

