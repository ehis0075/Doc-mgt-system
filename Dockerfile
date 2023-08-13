#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean package


#
# Package stage
#
FROM openjdk:8-jdk-alpinev

WORKDIR /app

COPY --from=build /home/app/target/dms-service.jar .

ENTRYPOINT ["java", "-jar","dms-service.jar"]
