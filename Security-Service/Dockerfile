FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} securityservice.jar
ENTRYPOINT ["java","-jar","/securityservice.jar"]
EXPOSE 4001