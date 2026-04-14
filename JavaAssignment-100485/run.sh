#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
mkdir -p out
javac --module-path libs --add-modules javafx.controls \
      -cp libs/postgresql-42.7.3.jar \
      -d out src/com/inventory/*.java
java  --module-path libs --add-modules javafx.controls \
      -cp "out:libs/postgresql-42.7.3.jar" \
      com.inventory.Main
