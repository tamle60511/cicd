FROM registry:latest
LABEL source=tamle6099
LABEL category=infrastructure
# COPY ["./tls-auth-proxy.conf", \
#  "./localhost.crt", \
#  "./localhost.key", \
#  "./registry.password", \
#  "/etc/nginx/conf.d/"]
CMD [ "/proxy.yml" ]
COPY ["./proxy.yml", \
    "./localhost.crt", \
    "./localhost.key", \
    "./registry.password", \
    "/etc/nginx/conf.d/"]