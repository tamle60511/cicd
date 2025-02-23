FROM registry:2
LABEL source=gianglt1
LABEL category=infrastructure
CMD ["/tls-auth-registry.yml"]
COPY ["./tls-auth-registry.yml", \
 "./localhost.crt", \
 "./localhost.key", \
 "./registry.password", \
 "/"]