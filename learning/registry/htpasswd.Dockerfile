FROM debian:latest
LABEL source=gianglt1
LABEL category=utility
RUN apt-get update && \
 apt-get install -y apache2-utils
ENTRYPOINT ["htpasswd"]