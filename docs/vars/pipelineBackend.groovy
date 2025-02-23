#!/usr/bin/env groovy

def call(String service = 'cms') {
    node {
        def DOCKER_REGISTRY = 'docker-hub'
        def DOCKER_REPO = 'tamle6099'
        def DOCKER_TAG = 'latest'
        def scmVars = checkout scm
        def app
        def BRANCH_NAME = "stg"
        def tag = ""
        def version = ""
        def envMode = "stg"
        def gitName = "tamle60511"
        def gitEmail = "tamle6099@gmail.com"
        def pipelineToken = "123456"
        def JENKINS_URL = "127.0.0.1:8080"

        try {
            docker.withRegistry('', 'docker-hub') {
                stage('CHECKOUT') {
                    sh 'pwd'
                    echo "Current process for ${scmVars.GIT_BRANCH}"
                    echo "Commit: ${scmVars.GIT_COMMIT}"
                    if (scmVars.GIT_BRANCH.contains('origin/main')) {
                        envMode = 'prd'
                    }
                    if (scmVars.GIT_BRANCH.contains('origin/stg')) {
                        envMode = 'stg'
                    }
                    BRANCH_NAME = scmVars.GIT_BRANCH.split('/')[-1]
                    echo "${scmVars}"

                    echo "env: ${envMode} - branch: ${BRANCH_NAME}"
                }

                stage('TEST') {
                    echo "Testing DONE ...."
                }

                stage('BUILD TAG COMMIT') {
                    sh 'git fetch --tags'
                    sh 'git fetch origin'

                    def curVersion = sh(script: "git tag --sort version:refname | tail -1 ", returnStdout: true).trim()
                    echo "version: ${curVersion}"

                    def versions = curVersion.split(("\\."))
                    echo "versions: ${versions}"

                    nextTag = versions[2].toInteger() + 1
                    tag = versions[0] + "." + versions[1] + "." + nextTag.toString() 
                    echo "tag: ${tag}"
                    echo "Git URL: ${scmVars.GIT_URL}"
                    def url = scmVars.GIT_URL.substring(8)
                    
                    def gitCommitID = sh(script: "git log --pretty=format:'%h' -n 1", returnStdout: true).trim()
                    def message =  sh(script: "git log --format=%B -n 1 ${gitCommitID}", returnStdout: true).trim()
                    echo "message: ${message}"
                    withCredentials([usernamePassword(credentialsId: "github-tamle60511", passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                        def uri ="https://${GIT_USERNAME}:${GIT_PASSWORD}@${url}"
                        sh "git config user.email \"${gitEmail}\""
                        sh "git config user.name \"${gitName}\""
                        sh "git tag -a ${tag} -m \"${message}\""
                        sh "git push ${uri} --tags"
                    }
                }

                stage('BUILD IMAGE') {
                    def GIT_COMMIT = scmVars.GIT_COMMIT
                    def GIT_BRANCH = scmVars.GIT_BRANCH
                    echo GIT_COMMIT
                    echo GIT_BRANCH
                    echo "BUILD TAG: ${service}:${BRANCH_NAME}-${env.BUILD_NUMBER}"
                    version = "${tag}-${BRANCH_NAME}-${env.BUILD_NUMBER}"
                    imageName = "${service}:${version}"

                    // DOCKER_TAG="${GIT_BRANCH.tokenize('/').pop()}-${env.BUILD_NUMBER}-${GIT_COMMIT.substring(0,7)}"
                    echo imageName
                    app = docker.build("${DOCKER_REPO}/${imageName}")
                }

                stage('PUSH IMAGE') {
                    app.push('latest')
                }

                stage('TRIGGER TO DEPLOY') {
                    def adminToken = "admin:11c800911486b01c1a2715cad8e6355ead"
                    sh ("curl -u ${adminToken} http://${adminToken}@${JENKINS_URL}/job/cd-pipeline/buildWithParameters?token=${pipelineToken} --data branch=${envMode} --data app=${service} --data tag=${tag}")
                    echo "Trigger to deploy DONE!!!"
                }
            }
            
        } catch (Exception err) {
            echo "${err}"
            if (err.getMessage() != "FILEEXITED"){
                currentBuild.result = "FAILURE"
                print "Failed with reason: ${err}"
                throw err
            }
        } finally {
            print "Close"
        }
    }

}