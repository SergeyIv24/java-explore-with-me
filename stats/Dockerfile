FROM amazoncorretto:21-alpine
COPY stats-server/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]