# Build stage
FROM maven:3.8-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM adoptopenjdk/openjdk11:jre-11.0.14_9-alpine
COPY --from=build /home/app/target/DarkShard-1.0.jar /usr/local/lib/DarkShard.jar
ENV PORT=50051
EXPOSE ${PORT}
COPY bin/wait-for.sh wait-for.sh
RUN chmod +x wait-for.sh
ENTRYPOINT [ "/bin/sh", "-c" ]
# RabbitMQ and other dependencies will probably need to be waited for before we start the app
# source: https://github.com/eficode/wait-for
CMD ["./wait-for.sh rabbitmq:5672 --timeout=300 -- java -jar /usr/local/lib/DarkShard.jar"]