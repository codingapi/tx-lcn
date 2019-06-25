FROM gtavali/alpine-oraclejdk8:latest

VOLUME /tmp
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ARG JAR_FILE
ADD ${JAR_FILE} app.jar

EXPOSE 8080 8070

ENTRYPOINT ["java","-jar", "/app.jar","-Duser.timezone=GMT+08", "-Djava.security.egd=file:/dev/./urandom"]