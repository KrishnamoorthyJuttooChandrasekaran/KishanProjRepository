FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} farmerservice.jar
ENTRYPOINT ["java","-jar","/farmerservice.jar"]
EXPOSE 4001