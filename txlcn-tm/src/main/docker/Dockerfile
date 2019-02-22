FROM frolvlad/alpine-oraclejdk8:slim
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone
COPY txlcn-tm-5.0.2.RELEASE.jar tm.jar
EXPOSE 7970 8070
ENTRYPOINT ["java", "-jar", "/tm.jar"]
