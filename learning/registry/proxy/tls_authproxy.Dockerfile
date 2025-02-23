FROM nginx:latest
LABEL source=tamle6099
LABEL category=infrastructure
COPY ["./tls-auth-proxy.conf", \
    "./localhost.crt", \
    "./localhost.key", \
    "./registry.password", \
    "/etc/nginx/conf.d/"]