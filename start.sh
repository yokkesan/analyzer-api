#!/bin/sh

./gradlew classes --continuous &

./gradlew bootRun

# ./gradlew bootRun -t