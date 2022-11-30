FROM openjdk:20-oracle

COPY target/channel-*.jar /app.jar

EXPOSE 8080

CMD java -jar /app.jar