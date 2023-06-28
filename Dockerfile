FROM maven:3-amazoncorretto-20-debian AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM openjdk:20-jdk-slim-buster

COPY --from=build /app/target/puzzles-0.0.1-SNAPSHOT.jar puzzles.jar

EXPOSE $PORT

ENTRYPOINT ["java", "-jar", "puzzles.jar"]