#!/usr/bin/env bash

# build
mvn clean package

# apply migrations
java -jar target/dropwizard-0.0.1-SNAPSHOT.jar db migrate filestore.yaml

# run the app
java -jar target/dropwizard-0.0.1-SNAPSHOT.jar server filestore.yaml
