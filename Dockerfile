#
# Build stage
#
FROM maven:3-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean package


#
# Package stage
#
FROM amazoncorretto:17

WORKDIR /app

COPY --from=build /home/app/target/dms-service.jar .

ENTRYPOINT ["java", "-jar","dms-service.jar"]



