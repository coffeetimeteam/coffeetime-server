FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/coffeetime-server-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} coffeetime.jar

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul", "coffeetime.jar"]