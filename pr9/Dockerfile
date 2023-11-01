FROM golang:1.21.1-bookworm AS build

RUN mkdir /src

WORKDIR /src

COPY . ./

RUN apt-get update && \
  apt-get install -y build-essential

RUN go get -d -v -t
RUN make build
RUN chmod +x ./app

FROM alpine:3.18.4
COPY --from=build /src/app /usr/local/bin/app

EXPOSE 8080
CMD ["/usr/local/bin/app"]
