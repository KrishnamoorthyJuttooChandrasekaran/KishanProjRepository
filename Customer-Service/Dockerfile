FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} customerservice.jar
ENTRYPOINT ["java","-jar","/customerservice.jar"]
EXPOSE 4000