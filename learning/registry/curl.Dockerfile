FROM alpine:latest
LABEL source=gianglt1
LABEL category=utility
RUN apk --update add curl
ENTRYPOINT ["curl"]
CMD ["--help"]