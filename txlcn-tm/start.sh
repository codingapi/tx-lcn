#!/bin/bash

echo 'docker stop tx-manager...'
docker stop tx-manager

echo 'docker rm tx-manager...'
docker rm tx-manager

docker run -p 7970:7970 -p 8070:8070 \
 -e "server.port=7970" \
 -e "spring.redis.host=192.168.110.129" \
 -e "spring.datasource.url=jdbc:mysql://192.168.110.1:3306/tx-manager?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false" \
 -e "spring.datasource.username=root" \
 -e "spring.datasource.password=root" \
 -e "tx-lcn.manager.host=192.168.110.129" \
 --name=tx-manager 119.yizhishang.com:5000/yizhishang/tx-manager:latest