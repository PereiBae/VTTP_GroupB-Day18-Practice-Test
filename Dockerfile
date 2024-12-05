FROM openjdk:23-jdk-oracle AS builder

ARG COMPILE_DIR=/compiledir

WORKDIR ${COMPILE_DIR}

COPY mvnw .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

## Build the application
RUN ./mvnw package -Dmaven.test.skip=true

## How to run the applicaiton
#ENV SERVER_PORT=8080
# for Railway
ENV PORT=3000
ENV SPRING_DATA_REDIS_USERNAME=""
ENV SPRING_DATA_REDIS_PASSWORD=""
ENV SPRING_DATA_REDIS_HOST=localhost
ENV SPRING_DATA_REDIS_PORT=6379
ENV SPRING_DATA_REDIS_DATABASE=0

EXPOSE ${PORT}

# App will run in second stage
# ENTRYPOINT SERVER_PORT=${PORT} java -jar target/Day18-0.0.1-SNAPSHOT.jar

## Day 18 - slide 13
## The second stage
FROM openjdk:23-jdk-oracle

ARG WORK_DIR=/app

WORKDIR ${WORK_DIR}

COPY --from=builder /compiledir/target/practice-test-0.0.1-SNAPSHOT.jar practiceTest.jar
COPY src/main/resources/static/todos.json /app/todos.json

ENV PORT=3000
ENV SPRING_DATA_REDIS_USERNAME=""
ENV SPRING_DATA_REDIS_PASSWORD=""
ENV SPRING_DATA_REDIS_HOST=localhost
ENV SPRING_DATA_REDIS_PORT=6379
ENV SPRING_DATA_REDIS_DATABASE=0

EXPOSE ${PORT}

HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 CMD curl -s -f http://localhost:3000/health || exit 1

ENTRYPOINT java -jar practiceTest.jar