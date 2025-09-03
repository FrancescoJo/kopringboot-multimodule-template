#!/bin/bash
set -e

# Script to validate coverage thresholds for changed modules
# Usage: code_quality_validate_coverage.sh <changed_modules> [instruction_threshold] [branch_threshold]
# Example: code_quality_validate_coverage.sh "app-backend admin-backend" 25 25

CHANGED_MODULES="$1"
TARGET_INSTRUCTION_COVERED="${2:-25}"
TARGET_BRANCH_COVERED="${3:-25}"

if [ -z "$CHANGED_MODULES" ]; then
    echo "No changed modules detected. Skipping coverage validation."
    exit 0
fi

# Validate coverage thresholds for each changed module
COVERAGE_FAILED=false
echo "========= Coverage measurements ========="
for module in $CHANGED_MODULES; do
    JACOCO_XML="$module/build/reports/jacoco/merged/$module-jacocoTestReport.xml"

    if [ ! -f "$JACOCO_XML" ]; then
        echo "::error::Coverage report not found for module $module at $JACOCO_XML"
        COVERAGE_FAILED=true
        continue
    fi

    INSTRUCTION_COVERED=$(xmllint --xpath 'string(//report/counter[@type="INSTRUCTION"]/@covered)' "$JACOCO_XML" 2>/dev/null || echo "0")
    INSTRUCTION_MISSED=$(xmllint --xpath 'string(//report/counter[@type="INSTRUCTION"]/@missed)' "$JACOCO_XML" 2>/dev/null || echo "0")
    BRANCH_COVERED=$(xmllint --xpath 'string(//report/counter[@type="BRANCH"]/@covered)' "$JACOCO_XML" 2>/dev/null || echo "0")
    BRANCH_MISSED=$(xmllint --xpath 'string(//report/counter[@type="BRANCH"]/@missed)' "$JACOCO_XML" 2>/dev/null || echo "0")

    # Calculate percentages
    if [ "$INSTRUCTION_COVERED" -gt 0 ] || [ "$INSTRUCTION_MISSED" -gt 0 ]; then
        INSTRUCTION_TOTAL=$((INSTRUCTION_COVERED + INSTRUCTION_MISSED))
        INSTRUCTION_PERCENTAGE=$((INSTRUCTION_COVERED * 100 / INSTRUCTION_TOTAL))
    else
        INSTRUCTION_PERCENTAGE=0
    fi

    if [ "$BRANCH_COVERED" -gt 0 ] || [ "$BRANCH_MISSED" -gt 0 ]; then
        BRANCH_TOTAL=$((BRANCH_COVERED + BRANCH_MISSED))
        BRANCH_PERCENTAGE=$((BRANCH_COVERED * 100 / BRANCH_TOTAL))
    else
        BRANCH_PERCENTAGE=100
    fi

    echo "Module '$module':"
    echo "  Instructions: $INSTRUCTION_PERCENTAGE% ($INSTRUCTION_COVERED/$INSTRUCTION_TOTAL)"
    echo "  Branches    : $BRANCH_PERCENTAGE% ($BRANCH_COVERED/$BRANCH_TOTAL)"

    if [ "$INSTRUCTION_PERCENTAGE" -lt $TARGET_INSTRUCTION_COVERED ]; then
        echo "Module '$module' instruction coverage (${INSTRUCTION_PERCENTAGE}%) is below ${TARGET_INSTRUCTION_COVERED}%" >&2
        COVERAGE_FAILED=true
    fi

    if [ "$BRANCH_PERCENTAGE" -lt $TARGET_BRANCH_COVERED ]; then
        echo "Module '$module' branch coverage (${BRANCH_PERCENTAGE}%) is below ${TARGET_BRANCH_COVERED}%" >&2
        COVERAGE_FAILED=true
    fi
done
echo "========================================="

if [ "$COVERAGE_FAILED" = true ]; then
    echo "Coverage validation failed. See the logs and fix them." >&2
    exit 1
fi
