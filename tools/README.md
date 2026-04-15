# Local Tooling

The `tools/` directory is for local development-time dependencies used by repository scripts. Files here are not shipped with the application.

## PlantUML

Phase 1 diagram rendering uses PlantUML pinned to `v1.2025.2`.

Download `plantuml-1.2025.2.jar` from the PlantUML releases page:

https://github.com/plantuml/plantuml/releases

Direct pinned download:

https://github.com/plantuml/plantuml/releases/download/v1.2025.2/plantuml-1.2025.2.jar

Save it locally as:

```text
tools/plantuml.jar
```

The render scripts can also download this jar automatically when `tools/plantuml.jar` is missing.

## License

PlantUML is GPL licensed. This repository invokes PlantUML as a standalone local tool and does not link against it. The local jar is ignored by Git via `tools/.gitignore`.

## Graphviz

Graphviz is optional for activity, class, and sequence diagrams, but may be needed for component diagram layouts.

Install Graphviz with:

```powershell
choco install graphviz
```

```bash
brew install graphviz
```

```bash
sudo apt install graphviz
```

## Quick Usage

Run one of these commands from the repository root:

```bash
bash scripts/render-diagrams.sh
```

```bat
scripts\render-diagrams.bat
```