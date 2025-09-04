#!/bin/bash
set -e

# Script to detect changed modules in a PR
# Usage: get_changed_modules.sh <CHECK_TARGET_MODULES> <base_ref>
# Example: get_changed_modules.sh "app-backend admin-backend" origin/main

CHECK_TARGET_MODULES_STR="$1"
BASE_REF="${2:-origin/main}"

if [ -z "$CHECK_TARGET_MODULES_STR" ]; then
    echo "Error: CHECK_TARGET_MODULES parameter is required"
    echo "Usage: $0 '<space-separated modules>' [base_ref]"
    exit 1
fi

# Convert string to array
read -ra CHECK_TARGET_MODULES <<< "$CHECK_TARGET_MODULES_STR"

# Get changed files in the PR
CHANGED_FILES=$(git diff --name-only ${BASE_REF}...HEAD)
echo "Changed files:"
echo "$CHANGED_FILES"
echo

# Find changed modules
CHANGED_MODULES=""
for module in "${CHECK_TARGET_MODULES[@]}"; do
    if echo "$CHANGED_FILES" | grep -q "^$module/"; then
        CHANGED_MODULES="$CHANGED_MODULES $module"
    fi
done

# Trim leading space and set outputs
CHANGED_MODULES=$(echo "$CHANGED_MODULES" | sed 's/^ *//')

# Output for GitHub Actions
if [ -n "$GITHUB_OUTPUT" ]; then
    echo "changed-modules=$CHANGED_MODULES" >> $GITHUB_OUTPUT
    echo "has-changes=$([ -n "$CHANGED_MODULES" ] && echo 'true' || echo 'false')" >> $GITHUB_OUTPUT
fi

if [ -n "$CHANGED_MODULES" ]; then
    echo "Changed modules: $CHANGED_MODULES"
else
    echo "No changes in target modules: ${CHECK_TARGET_MODULES[*]}"
fi
