FROM java:8
VOLUME /tmp

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./target/tx-manager-4.1.0.zip /tx-manager-4.1.0.zip
RUN bash -c 'unzip /tx-manager-4.1.0.zip'
RUN bash -c 'touch /tx-manager-4.1.0/tx-manager-4.1.0.jar'
EXPOSE 8899
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "/tx-manager-4.1.0/tx-manager-4.1.0.jar", "-Xms", "356m", "-Xmx", "356m"]


# docker运行命令如下：
# docker run --rm -p8899:8899 -p9999:9999 -e eureka.client.service-url.defaultZone=http://172.16.14.163:8761/eureka/ -e spring.redis.host=172.16.14.163 wine6823/tx-manager
#
# 上面命令有两个需自定义的参数：
# eureka.client.service-url.defaultZone=http://172.16.14.163:8761/eureka/
# spring.redis.host=172.16.14.163