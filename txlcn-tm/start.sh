#!/bin/bash

docker stop tx-manager
docker rm tx-manager

docker run -e PARAMS="--spring.redis.host=192.168.110.119 --spring.redis.database=0 --tx-lcn.manager.host=192.168.110.119" \
 -p 7970:7970 -p 8070:8070 \
 --name=tx-manager 119.yizhishang.com:5000/yizhishang/tx-manager:latest