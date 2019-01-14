/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.manager.db.redis;

import com.codingapi.txlcn.manager.config.TxManagerConfig;
import com.codingapi.txlcn.manager.db.ManagerStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Date: 2018/12/28
 *
 * @author ujued
 */
@Component
@Slf4j
public class RedisManagerStorage implements ManagerStorage, DisposableBean {

    private static final String REDIS_PREFIX = "tx.manager.list";

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    private TxManagerConfig managerConfig;

    @Value("${server.port}")
    private int port;


    @Autowired
    public RedisManagerStorage(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private boolean add(String address) {
        log.info(address);
        List<String> list = list();
        if(list==null){
            list = new ArrayList<>();
        }
        if(list.contains(address)){
            return false;
        }
        list.add(address);
        save(list);
        return true;
    }

    private void save(List<String> list){
        String[] array=list.toArray(new String[list.size()]);
        String addressList = String.join(",",array);
        redisTemplate.opsForValue().set(REDIS_PREFIX,addressList);
    }


    private List<String> list() {
        String addressList = redisTemplate.opsForValue().get(REDIS_PREFIX);
        if(addressList==null){
            return null;
        }
        return new ArrayList<>(Arrays.asList(addressList.split(",")));
    }

    @Override
    public List<String> addressList() {
        List<String> list = list();
        if(list==null){
            return null;
        }
        String address = managerConfig.getHost()+":"+port;
        list.remove(address);
        return list;
    }


    @Override
    public void remove(String address) {
        List<String> list = list();
        list.remove(address);
        if(list.size()==0){
            redisTemplate.delete(REDIS_PREFIX);
        }else{
            save(list);
        }
    }


    @PostConstruct
    public void init(){
        String address = managerConfig.getHost()+":"+port;
        add(address);
        log.info("manager add redis finish.");
    }

    @Override
    public void destroy() throws Exception {
        String address = managerConfig.getHost()+":"+port;
        remove(address);
        log.info("manager remove redis.");
    }
}
