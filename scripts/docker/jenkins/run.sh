#!/bin/bash
# exit immediately if a command exits with a non-zero status.
set -e

BUILDMODE=""
NETWORK="jenkins"

for i in "$@"
do
    case $i in
        -t=*|--type=*)
            BUILDMODE="${i#*=}"
            shift
            ;;
        -n=*|--network=*)
            NETWORK="${i#*=}"
            shift
            ;;
        *)
            # Ignore other arguments
            ;;
    esac
done



buildImageJenkins(){
    docker build -t jenkins_jenkins -f Dockerfile .
}

buildImageJenkinsDocker(){
    docker run --name jenkins-docker --rm --detach \
    --privileged --network $NETWORK --net-alias docker \
    --env DOCKER_TLS_CERTDIR=/certs \
    --volume ~/build/cicd/jenkins-docker-certs:/certs/client \
    --volume ~/build/cicd/jenkins-data:/var/jenkins_home \
    --publish 2376:2376 \
    --publish 2377:2375 \
    docker:dind --storage-driver overlay2
}

buildJenkins(){
    docker run --name jenkins --rm --detach --privileged \
    --network $NETWORK --env DOCKER_HOST=tcp://docker:2376 \
    --env DOCKER_CERT_PATH=/certs/client --env DOCKER_TLS_VERIFY=1 \
    --link jenkins-docker:docker \
    --publish 8081:8080 --publish 50000:50000 \
    --volume ~/build/cicd/jenkins-data:/var/jenkins_home \
    --volume ~/build/cicd/jenkins-docker-certs:/certs/client:ro \
    --volume /var/run/docker.sock:/var/run/docker.sock \
    jenkins_jenkins
    # docker-compose -f ./jenkins.yml up -d
}

uninstallJenkins(){
    docker rm -vf jenkins jenkins-docker
    docker image rm -f jenkins_jenkins
}

echo $BUILDMODE
if [ -z $BUILDMODE ]; then 
    echo "BUILD ALL"
    buildImageJenkins
    buildImageJenkinsDocker
    buildJenkins
elif [ "$BUILDMODE" = "image" ]; then
    echo "BUILD IMAGE JENKINS"
    buildImageJenkins
elif [ "$BUILDMODE" = "runner" ]; then
    echo "BUILD RUNNER JENKINS"
    buildJenkins
elif [ "$BUILDMODE" = "dind" ]; then 
    echo "BUILD DIND JENKINS"
    buildImageJenkinsDocker
elif [ "$BUILDMODE" = "uninstall" ]; then
    echo "UNINSTALL RUNNER JENKINS"
    uninstallJenkins
fi
