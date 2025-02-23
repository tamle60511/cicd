#!/bin/bash

buildSocat(){
    docker run -d --name socat \
        --publish 2377:2375 \
        --volume /var/run/docker.sock:/var/run/docker.sock \
        alpine/socat tcp-listen:2375,fork,reuseaddr unix-connect:/var/run/docker.sock
}

buildSocat