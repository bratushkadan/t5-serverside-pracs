.PHONY: build-cloud
build-cloud:
	@export CGO_ENABLED=0 GOOS=linux; go build -v -o app

.PHONY: build
build:
	@export CGO_ENABLED=0; go build -v -o app
