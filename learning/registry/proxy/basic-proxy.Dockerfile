FROM nginx:latest
LABEL source=tamle6099
LABEL category=infrastructure
COPY ./basic-proxy.conf /etc/nginx/conf.d/default.conf