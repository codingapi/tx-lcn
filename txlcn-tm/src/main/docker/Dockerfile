FROM openjdk:8-jdk-alpine

VOLUME /tmp
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ARG JAR_FILE
ADD ${JAR_FILE} app.jar

#EXPOSE 8080 8070
#EXPOSE 8070

ENTRYPOINT ["/bin/sh", "-c", "java $JAVA_OPTS -jar /app.jar -Djava.security.egd=file:/dev/./urandom"]

#ENTRYPOINT ["java","-jar", "/app.jar","$JAVA_OPTS -Duser.timezone=GMT+08", "-Djava.security.egd=file:/dev/./urandom"]