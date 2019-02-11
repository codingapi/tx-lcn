FROM java:8-jre-alpine

RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone

COPY txlcn-tm-5.0.1.RELEASE.jar tm.jar
EXPOSE 7970 8070
ENTRYPOINT ["java", "-jar", "/tm.jar"]