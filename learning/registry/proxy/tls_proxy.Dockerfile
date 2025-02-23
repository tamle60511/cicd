FROM nginx:latest
LABEL source=tamle6099
LABEL category=infrastructure
COPY ["./tls_proxy.conf", \
    "./localhost.crt", \
    "./localhost.key", \
    "/etc/nginx/conf.d/"]