/**
 * Application composition and GUI-facing application services.
 *
 * <p><b>Intended dependency direction:</b> {@code mapsaroundyou.gui} (JavaFX) depends on narrow
 * service types defined here (for example {@link mapsaroundyou.app.GuiSearchService}). Those
 * services delegate to {@link mapsaroundyou.logic.SearchLogic}, which depends only on
 * {@link mapsaroundyou.storage} abstractions (repositories) and {@link mapsaroundyou.service}
 * helpers. Concrete CSV and properties adapters live in {@code mapsaroundyou.storage} and are
 * wired only from {@link mapsaroundyou.app.ApplicationFactory}.
 */
package mapsaroundyou.app;
