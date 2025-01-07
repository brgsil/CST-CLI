#!/bin/bash

docker run \
    -it \
    --rm \
    -d \
    -v $(pwd):/app \
    -w /app \
    --name cst-test \
    openjdk:17-slim

docker exec cst-test ./gradlew test

docker stop cst-test
