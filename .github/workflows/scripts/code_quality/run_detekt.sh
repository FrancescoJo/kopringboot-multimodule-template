#!/bin/bash
set -e

# Script to run detekt on changed modules
# Usage: run_detekt.sh <changed_modules>
# Example: run_detekt.sh "app-backend admin-backend"

CHANGED_MODULES="$1"

if [ -z "$CHANGED_MODULES" ]; then
    echo "Error: CHANGED_MODULES parameter is required"
    echo "Usage: $0 '<space-separated modules>'"
    exit 1
fi

DETEKT_TARGETS=""

for module in $CHANGED_MODULES; do
    DETEKT_TARGETS="$DETEKT_TARGETS :$module:detektAll"
done

echo "Running detekt on targets:$DETEKT_TARGETS"
./gradlew $DETEKT_TARGETS
