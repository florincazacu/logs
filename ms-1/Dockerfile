FROM eclipse-temurin:21-alpine
VOLUME /tmp
ADD target/ms-1-1.0.0-SNAPSHOT.jar app.jar
CMD ["sh", "-c", "java -jar /app.jar"]