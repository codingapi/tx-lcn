FROM 192.168.110.119:5000/library/openjdk:8-jdk-alpine

VOLUME /tmp
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ARG JAR_FILE
ADD ./txlcn-tm/target/tx-manager.jar app.jar

ENTRYPOINT ["/bin/sh", "-c", "java $JAVA_OPTS -jar /app.jar -Djava.security.egd=file:/dev/./urandom"]
