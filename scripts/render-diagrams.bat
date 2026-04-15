@echo off
setlocal enabledelayedexpansion

set "REPO_ROOT=%~dp0.."
set "PLANTUML_VERSION=1.2025.2"
set "PLANTUML_JAR_URL=https://github.com/plantuml/plantuml/releases/download/v1.2025.2/plantuml-1.2025.2.jar"
set "PLANTUML_JAR_PATH=%REPO_ROOT%\tools\plantuml.jar"
set "DIAGRAM_DIR=%REPO_ROOT%\docs\design\diagrams"

if not exist "%PLANTUML_JAR_PATH%" (
    curl --version >nul 2>&1
    if not errorlevel 1 (
        if not exist "%REPO_ROOT%\tools" mkdir "%REPO_ROOT%\tools"
        echo Downloading PlantUML %PLANTUML_VERSION%...
        curl -L -o "%PLANTUML_JAR_PATH%" "%PLANTUML_JAR_URL%"
        if errorlevel 1 exit /b 1
        echo Downloaded PlantUML to %PLANTUML_JAR_PATH%
    ) else (
        if not exist "%REPO_ROOT%\tools" mkdir "%REPO_ROOT%\tools"
        powershell -Command "Invoke-WebRequest -Uri '%PLANTUML_JAR_URL%' -OutFile '%PLANTUML_JAR_PATH%'"
        if errorlevel 1 exit /b 1
        echo Downloaded PlantUML to %PLANTUML_JAR_PATH%
    )
)

if not exist "%DIAGRAM_DIR%" mkdir "%DIAGRAM_DIR%"

dir /b "%DIAGRAM_DIR%\*.puml" >nul 2>&1
if errorlevel 1 (
    echo Info: no .puml files found in docs\design\diagrams; nothing to render.
    exit /b 0
)

java -jar "%PLANTUML_JAR_PATH%" -tsvg -o . "%DIAGRAM_DIR%\*.puml"
if errorlevel 1 exit /b 1

echo Produced SVG files:
for %%F in ("%DIAGRAM_DIR%\*.svg") do (
    if exist "%%~fF" echo %%~fF
)