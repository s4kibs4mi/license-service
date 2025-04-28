.PHONY: build run run_app test all

all: build test run

build:
	./gradlew :bootJar

run:
	java -jar ./build/libs/license-service*.jar

run_app:
	docker-compose up --build

test:
	./gradlew :test