FROM nginx:latest
LABEL source=gianglt1
LABEL category=infrastructure
COPY ./basic-proxy.conf /etc/nginx/conf.d/default.conf