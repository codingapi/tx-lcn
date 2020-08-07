package com.codingapi.txlcn.tm.repository;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lorne
 * @date 2020/8/7
 * @description
 */
class TransactionGroupTest {

    @Test
    public void jsonParser(){
        String json = "[\n" +
                "    \"com.codingapi.txlcn.tm.repository.TransactionGroup\",\n" +
                "    {\n" +
                "        \"groupId\": \"379facf8-31db-4a1d-98d3-d895cfcf1ad1\",\n" +
                "        \"state\": \"JOIN\",\n" +
                "        \"transactionInfoList\": [\n" +
                "            \"java.util.ArrayList\",\n" +
                "            [\n" +
                "                [\n" +
                "                    \"com.codingapi.txlcn.tm.repository.TransactionInfo\",\n" +
                "                    {\n" +
                "                        \"moduleName\": \"example-tc\",\n" +
                "                        \"uniqueKey\": \"127.0.0.1:64724\",\n" +
                "                        \"time\": 1596787596025,\n" +
                "                        \"transactionType\": \"REQUEST\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            ]\n" +
                "        ]\n" +
                "    }\n" +
                "]";

        Jackson2JsonRedisSerializer<TransactionGroup> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<TransactionGroup>(TransactionGroup.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        TransactionGroup data = (TransactionGroup)jackson2JsonRedisSerializer.deserialize(json.getBytes());
        System.out.println(data);
    }
}
