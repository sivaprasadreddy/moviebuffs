#!/bin/bash

echo "Branch: $TRAVIS_BRANCH, TRAVIS_PULL_REQUEST: $TRAVIS_PULL_REQUEST"
if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
    echo "Deploying on Heroku"
    HEROKU_API_KEY="$HEROKU_API_KEY" ./mvnw clean heroku:deploy
else
    echo "Not master branch, so Heroku deployment is skipped."
fi
