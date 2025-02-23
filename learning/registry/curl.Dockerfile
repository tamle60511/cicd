FROM alpine:latest
LABEL source=tamle6099
LABEL category=utility
RUN apk --update add curl
ENTRYPOINT ["curl"]
CMD ["--help"]