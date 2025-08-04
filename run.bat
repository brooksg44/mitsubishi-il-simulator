@echo off
REM Mitsubishi IL Simulator Startup Script for Windows

echo Starting Mitsubishi IL Simulator...
echo ======================================

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo Error: Java is not installed or not in PATH
    echo Please install Java 11 or higher with JavaFX support
    pause
    exit /b 1
)

REM Check if Leiningen is installed
lein version >nul 2>&1
if errorlevel 1 (
    echo Error: Leiningen is not installed or not in PATH
    echo Please install Leiningen from https://leiningen.org/
    pause
    exit /b 1
)

echo Java version:
java -version 2>&1 | findstr "version"

echo Leiningen version:
lein version | findstr "Leiningen"

echo.

REM Change to the project directory
cd /d "%~dp0"

REM Install dependencies if needed
if not exist "target" (
    echo Installing dependencies...
    lein deps
)

REM Run the simulator
echo Launching Mitsubishi IL Simulator GUI...
echo Press Ctrl+C to stop the simulator
echo.

REM Set JavaFX module path for newer Java versions
set JVM_OPTS=--add-modules javafx.controls,javafx.fxml,javafx.swing,javafx.web --add-exports javafx.base/com.sun.javafx.runtime=ALL-UNNAMED

lein run

echo.
echo Simulator stopped.
pause
