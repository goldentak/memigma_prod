FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/project-0.0.1-SNAPSHOT.jar memigma-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "memigma-app.jar"]
