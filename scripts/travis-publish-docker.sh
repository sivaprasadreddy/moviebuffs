#!/bin/bash

echo "Branch: $TRAVIS_BRANCH, TRAVIS_PULL_REQUEST: $TRAVIS_PULL_REQUEST"
if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
    echo "Building and pushing Docker Image to DockerHub"
    docker login -u $DOCKER_USER -p $DOCKER_PASS
    export TAG=`if [ "$TRAVIS_BRANCH" == "master" ]; then echo "latest"; else echo $TRAVIS_BRANCH; fi`
    export DOCKER_IMAGE=$DOCKER_USER/$SPRING_BOOT_APP_NAME
    docker build -t $DOCKER_IMAGE:$COMMIT .
    docker tag $DOCKER_IMAGE:$COMMIT $DOCKER_IMAGE:$TAG
    docker push $DOCKER_IMAGE
else
    echo "Not master branch, so building docker image is skipped."
fi