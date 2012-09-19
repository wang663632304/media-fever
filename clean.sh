#!/bin/sh

# Media Fever Server
cd media-fever-server
mvn dependency:resolve
cd ..

# Media Fever Android
cd media-fever-android
mvn dependency:resolve
cd ..

