FROM nginx:latest
LABEL source=gianglt1
LABEL category=infrastructure
COPY ["./tls_proxy.conf", \
 "./localhost.crt", \
 "./localhost.key", \
 "/etc/nginx/conf.d/"]