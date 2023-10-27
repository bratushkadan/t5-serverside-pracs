FROM gradle:8.4.0-jdk17 AS build

WORKDIR /app

COPY ./java-app /app

RUN gradle build  

FROM openjdk:17

WORKDIR /app

COPY --from=build /app/build/libs/app.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
