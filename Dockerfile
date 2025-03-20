FROM openjdk:19-jdk-slim-buster
WORKDIR /app
COPY target/demo-0.0.1-SNAPSHOT.jar /app/rest-app.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","rest-app.jar"]