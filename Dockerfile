FROM node:16.20.0 AS react-build

WORKDIR /react-app

RUN git clone https://github.com/mikhalov/puzzles-frontend.git .

RUN npm install

RUN npm run build

FROM maven:3-amazoncorretto-20-debian AS build

WORKDIR /app

COPY pom.xml .

COPY src ./src
RUN mkdir -p src/main/resources/static
COPY --from=react-build /react-app/build ./src/main/resources/static

RUN mvn clean package

FROM openjdk:20-jdk-slim-buster

COPY --from=build /app/target/puzzles-0.0.1-SNAPSHOT.jar puzzles.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE $PORT

ENTRYPOINT ["java", "-jar", "puzzles.jar"]