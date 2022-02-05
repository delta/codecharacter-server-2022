#!/bin/sh

echo "Running pre-commit checks..."

./gradlew check test --daemon

status=$?

exit $status
