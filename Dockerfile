FROM openjdk:12-jdk-alpine
COPY "./target/*.jar" "app.jar"
COPY "./src/main/resources/application.docker.properties" "application.properties"
COPY "./src/main/resources/configurations.docker.properties" "configurations.properties"
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]