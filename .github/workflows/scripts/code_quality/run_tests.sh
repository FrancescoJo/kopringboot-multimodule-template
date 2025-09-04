#!/bin/bash
set -e

# Script to run tests on changed modules
# Usage: run_tests.sh <changed_modules>
# Example: run_tests.sh "app-backend admin-backend"

CHANGED_MODULES="$1"

if [ -z "$CHANGED_MODULES" ]; then
    echo "Error: CHANGED_MODULES parameter is required"
    echo "Usage: $0 '<space-separated modules>'"
    exit 1
fi

TEST_TARGETS=""

for module in $CHANGED_MODULES; do
    TEST_TARGETS="$TEST_TARGETS :$module:testAll"
done

echo "Running tests on targets:$TEST_TARGETS"
./gradlew $TEST_TARGETS
