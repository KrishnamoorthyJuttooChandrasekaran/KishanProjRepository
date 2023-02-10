FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} serviceregistory.jar
ENTRYPOINT ["java","-jar","/serviceregistory.jar"]
EXPOSE 8761