FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY ./build/libs/coffeetime-server-0.0.1-SNAPSHOT.jar /app/coffeetime.jar

CMD ["java", "-jar", "coffeetime.jar"]
