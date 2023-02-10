FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} cloudconfigserver.jar
ENTRYPOINT ["java","-jar","/cloudconfigserver.jar"]
EXPOSE 9296