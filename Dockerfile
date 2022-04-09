FROM openjdk:17.0.1-jdk-slim AS builder
WORKDIR application
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM openjdk:17.0.1-jdk-slim
WORKDIR application
ENV server.port 8080
ARG JAVA_OPTION=""
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "-Dspring.profiles.active=oauth", "org.springframework.boot.loader.JarLauncher", "${JAVA_OPTION}"]