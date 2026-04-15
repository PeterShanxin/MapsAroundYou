## Documentation maintenance (User Guide)

This note is for maintainers. It does not describe end-user behavior.

When user-facing CLI flags, interactive prompts, or GUI controls change, update `docs/UserGuide.md` by cross-checking:

- The CLI usage text printed by the app (`help`)
- The CLI parser (`src/main/java/mapsaroundyou/cli/CliCommandParser.java`)
- The interactive prompt flow (`src/main/java/mapsaroundyou/cli/CliApplication.java`)
- The GUI labels/controls (`src/main/java/mapsaroundyou/gui/MapsAroundYouGuiApp.java`)

