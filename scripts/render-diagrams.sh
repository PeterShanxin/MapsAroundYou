#!/usr/bin/env bash
set -euo pipefail

PLANTUML_VERSION=1.2025.2
PLANTUML_JAR_URL=https://github.com/plantuml/plantuml/releases/download/v1.2025.2/plantuml-1.2025.2.jar
PLANTUML_JAR_PATH=tools/plantuml.jar

if [ ! -f "$PLANTUML_JAR_PATH" ]; then
    if command -v curl >/dev/null 2>&1; then
        mkdir -p tools
        echo "Downloading PlantUML $PLANTUML_VERSION..."
        curl -fL -o "$PLANTUML_JAR_PATH" "$PLANTUML_JAR_URL"
        echo "Downloaded PlantUML to $PLANTUML_JAR_PATH"
    else
        echo "Error: tools/plantuml.jar is missing and curl was not found."
        echo "Download PlantUML manually from:"
        echo "$PLANTUML_JAR_URL"
        echo "Save it as $PLANTUML_JAR_PATH"
        exit 1
    fi
fi

mkdir -p docs/assets/images

puml_files=(docs/assets/images/*.puml)
if [ ! -e "${puml_files[0]}" ]; then
    echo "Info: no .puml files found in docs/assets/images; nothing to render."
    exit 0
fi

java -jar "$PLANTUML_JAR_PATH" -tsvg -o . docs/assets/images/*.puml

echo "Produced SVG files:"
svg_files=(docs/assets/images/*.svg)
for svg_file in "${svg_files[@]}"; do
    if [ -e "$svg_file" ]; then
        echo "$svg_file"
    fi
done
