#!/bin/bash

# Mitsubishi IL Simulator Startup Script

echo "Starting Mitsubishi IL Simulator..."
echo "======================================"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 11 or higher with JavaFX support"
    exit 1
fi

# Check if Leiningen is installed
if ! command -v lein &> /dev/null; then
    echo "Error: Leiningen is not installed or not in PATH"
    echo "Please install Leiningen from https://leiningen.org/"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 11 ]; then
    echo "Warning: Java version is $JAVA_VERSION, but Java 11+ is recommended"
fi

echo "Java version: $(java -version 2>&1 | head -1)"
echo "Leiningen version: $(lein version | head -1)"
echo

# Change to the project directory
cd "$(dirname "$0")"

# Install dependencies if needed
if [ ! -d "target" ]; then
    echo "Installing dependencies..."
    lein deps
fi

# Run the simulator
echo "Launching Mitsubishi IL Simulator GUI..."
echo "Press Ctrl+C to stop the simulator"
echo

# Run with Leiningen directly (JavaFX modules configured in project.clj)
lein run

echo
echo "Simulator stopped."
