FROM openjdk:17-jdk

COPY ./build/libs/coffeetime-server-0.0.1-SNAPSHOT.jar /app/coffeetime.jar

WORKDIR /app

CMD ["java", "-jar", "coffeetime.jar"]
