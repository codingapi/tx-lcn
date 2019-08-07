FROM openjdk:8-jdk-alpine

VOLUME /tmp
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["/bin/sh", "-c", "java $JAVA_OPTS -jar /app.jar -Djava.security.egd=file:/dev/./urandom"]
