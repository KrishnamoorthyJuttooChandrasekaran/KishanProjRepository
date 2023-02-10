FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} hystrixdashboard.jar
ENTRYPOINT ["java","-jar","/hystrixdashboard.jar"]
EXPOSE 4003