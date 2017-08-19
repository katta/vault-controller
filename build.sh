#!/usr/bin/env bash

set -e
set -x

mvn clean install -DskipTests

docker build -t vatsakatta/vault-controller:latest ./cobalt-vault-controller/
docker push vatsakatta/vault-controller:latest

docker build -t vatsakatta/token-retriever:latest ./cobalt-vault-token-retriever/
docker push vatsakatta/token-retriever:latest

docker build -t vatsakatta/vault-springapp:latest ./cobalt-vault-spring-app/
docker push vatsakatta/vault-springapp:latest