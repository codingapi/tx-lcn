FROM openjdk:8-jdk-alpine
VOLUME /tmp
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENV PARAMS=""

EXPOSE 7970 8070
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Duser.timezone=GMT+08 --spring.datasource.username=$spring_datasource_username --spring.datasource.url=$url --spring.datasource.password=$spring_datasource_password", "-jar","/app.jar"]
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Duser.timezone=GMT+08 --spring.redis.host=$spring_redis_host", "-jar","/app.jar"]

ENTRYPOINT ["sh","-c","java -jar /app.jar -Duser.timezone=GMT+08 -Djava.security.egd=file:/dev/./urandom $PARAMS"]