#!/usr/bin/env sh
set -e

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
cd "$SCRIPT_DIR"

mkdir -p out
find src/main/java -name "*.java" | xargs javac -d out
java -cp out com.dinereserve.DineReserveApplication "$@"
