#!/bin/bash

echo "Running pre-commit checks..."

TOP_DIR=$(git rev-parse --show-toplevel)

$TOP_DIR/gradlew spotlessApply --daemon

FILES=$((git diff --cached --name-only --diff-filter=ACM) || true)

git add $(echo $FILES | paste -sd " " -)

$TOP_DIR/gradlew test --daemon

status=$?

exit $status
