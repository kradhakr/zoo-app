FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/zoo-app-0.0.1-SNAPSHOT.jar zoo-app.jar
ENTRYPOINT ["java","-jar","/zoo-app.jar"]
