FROM openjdk:18
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} mueblesstgo-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/mueblesstgo-0.0.1-SNAPSHOT.jar"]