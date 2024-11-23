FROM amazoncorretto:17-alpine-jdk AS builder
COPY . /app
WORKDIR /app
RUN ["./gradlew", "clean", "bootJar"]

FROM amazoncorretto:17-alpine-jdk AS runner
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]


