#!/bin/sh

./gradlew classes --continuous &

./gradlew bootRun

# ./gradlew bootRun -t
# docker logs -f analyzer-api