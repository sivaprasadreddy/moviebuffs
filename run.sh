#!/bin/bash

declare project_dir=$(dirname $0)
declare dc_main=${project_dir}/docker/docker-compose.yml
declare dc_platform=${project_dir}/docker/docker-compose-sonar.yml
declare dc_elk=${project_dir}/docker/docker-compose-elk.yml
declare dc_monitoring=${project_dir}/docker/docker-compose-monitoring.yml
declare dc_test=${project_dir}/docker/docker-compose-test.yml
declare moviebuffs="moviebuffs"
declare sonarqube="sonarqube"
declare elk="elasticsearch logstash kibana"
declare monitoring="prometheus grafana"

function restart() {
    stop
    start
}

function start() {
    echo "Starting ${moviebuffs}...."
    build_docker_image
    docker-compose -f ${dc_main} up --build --force-recreate -d ${moviebuffs}
    docker-compose -f ${dc_main} logs -f
}

function stop() {
    echo "Stopping ${moviebuffs}...."
    docker-compose -f ${dc_main} stop
    docker-compose -f ${dc_main} rm -f
}

function start_all() {
    echo "Starting ${moviebuffs} and dependencies...."
    build_docker_image
    docker-compose -f ${dc_main} -f ${dc_elk} -f ${dc_monitoring} up --build --force-recreate -d
    docker-compose -f ${dc_main} -f ${dc_elk} -f ${dc_monitoring} logs -f
}

function stop_all() {
    echo 'Stopping all....'
    docker-compose -f ${dc_main} -f ${dc_elk} -f ${dc_monitoring} stop
    docker-compose -f ${dc_main} -f ${dc_elk} -f ${dc_monitoring} rm -f
}

function build_docker_image() {
    ./mvnw spring-boot:build-image -DskipTests
}

function build_api() {
    ./mvnw clean package -DskipTests
}

function sonar() {
    echo 'Starting sonarqube....'
    docker-compose -f ${dc_platform} up --build --force-recreate -d ${sonarqube}
    docker-compose -f ${dc_platform} logs -f
}

function elk() {
    echo 'Starting ELK....'
    docker-compose -f ${dc_elk} up --build --force-recreate -d ${elk}
    docker-compose -f ${dc_elk} logs -f
}

function monitoring() {
    echo 'Starting Prometheus, Grafana....'
    docker-compose -f ${dc_monitoring} up --build --force-recreate -d ${monitoring}
    docker-compose -f ${dc_monitoring} logs -f
}

function hubtest() {
    echo 'Run API from DockerHub....'
    docker-compose -f ${dc_test} up -d
    docker-compose -f ${dc_test} logs -f
}

action="start"

if [[ "$#" != "0"  ]]
then
    action=$@
fi

eval ${action}
