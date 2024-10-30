#!/bin/bash

docker run \
    -it \
    -d \
    -v $(pwd):/app \
    -w /app \
    --name cst-package \
    alpine/java:17-jdk

docker exec cst-package apk add rpm dpkg fakeroot

docker exec cst-package ./gradlew jpackage

docker exec cst-package chown -R $(id -u):$(id -g) build/

docker stop cst-package
docker remove cst-package
