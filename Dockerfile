# Build stage
FROM maven:3.8-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM adoptopenjdk/openjdk11:jre-11.0.14_9-alpine
COPY --from=build /home/app/target/DarkShard-1.0-SNAPSHOT.jar /usr/local/lib/DarkShard.jar
ENV PORT=50051
EXPOSE ${PORT}
CMD ["java", "-jar", "/usr/local/lib/DarkShard.jar"]