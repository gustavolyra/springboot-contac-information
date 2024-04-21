FROM openjdk:21-jdk
MAINTAINER glyra.com
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]