#!groovy
@Library('jenkins-shared-library')
import com.sivalabs.JenkinsSharedLib

properties([
    parameters([
        booleanParam(defaultValue: false, name: 'PUBLISH_TO_DOCKERHUB', description: 'Publish Docker Image to DockerHub?'),
        booleanParam(defaultValue: false, name: 'DEPLOY_ON_HEROKU', description: 'Should deploy on Heroku?'),
        booleanParam(defaultValue: false, name: 'RUN_PERF_TESTS', description: 'Should run Performance Tests?')
    ])
])

def DOCKER_USERNAME = 'sivaprasadreddy'
def API_IMAGE_NAME = 'moviebuffs-api'
def UI_IMAGE_NAME = 'moviebuffs-ui-react'

def utils = new JenkinsSharedLib(this, env, params, scm, currentBuild)

node {

    try {
        utils.checkout()

        dir("moviebuffs-ui-react") {
            utils.npmBuild("UI Build")
            utils.npmTest("UI Test")
        }

        dir("moviebuffs-api") {
            utils.runMavenTests("Tests")
            utils.runOWASPChecks("OWASP Checks")
            utils.publishDockerImage("Publish Docker", DOCKER_USERNAME, API_IMAGE_NAME)
            utils.deployOnHeroku("Heroku Deployment")
        }

        dir("moviebuffs-gatling-tests") {
            utils.runMavenGatlingTests("Perf Test")
        }
    }
    catch(err) {
        echo "ERROR: ${err}"
        currentBuild.result = currentBuild.result ?: "FAILURE"
    }
}
