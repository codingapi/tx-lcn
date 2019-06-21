FROM gtavali/alpine-oraclejdk8:latest

VOLUME /tmp
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENV PARAMS=""

EXPOSE 7970 8070

ENTRYPOINT ["sh","-c","java -jar /app.jar -Duser.timezone=GMT+08 -Djava.security.egd=file:/dev/./urandom $PARAMS"]