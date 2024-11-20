FROM ubuntu:22.04 AS build
LABEL authors="mathe"

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && \
        apt-get install -y openjdk-17-jdk maven && \
        apt-get clean && \
        rm -rf /var/lib/apt/lists/*

COPY . .

RUN mvn clean install -DskipTests

RUN ls -l /target

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]