FROM openjdk:20-oracle

COPY target/videos-*.jar /app.jar

EXPOSE 8080

CMD java -Dserver.port=$PORT $JAVA_OPTS -jar /app.jar