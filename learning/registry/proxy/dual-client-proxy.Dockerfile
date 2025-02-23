FROM nginx:latest
LABEL source=gianglt1
LABEL category=infrastructure
COPY ./dual-client-proxy.conf /etc/nginx/conf.d/default.conf