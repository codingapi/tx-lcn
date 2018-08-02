package com.codingapi.tx.datasource.relational.txc.rollback;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;

/**
 * @author jsy.
 * @title
 * @time 17/12/14.
 */

public class DiffUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException, JsonProcessingException {
                gen.writeString(value.setScale(2).toString());
            }
        });

        simpleModule.addSerializer(StringReader.class, new JsonSerializer<StringReader>() {
            @Override
            public void serialize(StringReader value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException, JsonProcessingException {
                gen.writeString(read(value));
            }
        });

        objectMapper.registerModule(simpleModule);
    }

    public  static boolean diff(Object oldDifDto, Object curDifDto) {
        try {
            String old = objectMapper.writeValueAsString(oldDifDto);

            String cur = objectMapper.writeValueAsString(curDifDto);
            JsonNode oldJsonNode = objectMapper.readTree(old);
            JsonNode curJsonNode = objectMapper.readTree(cur);

            if (oldJsonNode.equals(curJsonNode)) {
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }

    }


    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }


    public static String read(StringReader stringReader) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringReader.reset();
            int c;
            while ((c = stringReader.read()) != -1) {
                stringBuilder.append((char)c);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public static void main(String[] args) {
//        BigDecimal bigDecimal = new BigDecimal("50.00");
//        BigDecimal bigDecimal1 = new BigDecimal("50");
//
//        try {
//            String s = objectMapper.writeValueAsString(bigDecimal);
//            String s1 = objectMapper.writeValueAsString(bigDecimal1);
//            System.out.println(s);
//            System.out.println(s1);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        StringReader test = new StringReader("a:2:{s:20:\"php_serialize_option\";s:1:\" \";s:9:\"orderdata\";a:1:{i:0;a:22:{s:11:\"refund_type\";s:1:\"0\";s:8:\"dateline\";s:1:\"0\";s:11:\"mk_order_id\";s:4:\"null\";s:5:\"stype\";s:2:\"16\";s:11:\"sl_nickname\";s:10:\"zhigb_0016\";s:3:\"num\";s:1:\"1\";s:5:\"ptype\";s:2:\"23\";s:5:\"title\";s:55:\"lqq田酞递址秽梳猜欣勾翅#169779075稿件中标\";s:10:\"sl_user_id\";s:8:\"19182259\";s:7:\"link_id\";s:1:\"0\";s:11:\"offer_price\";s:3:\"0.0\";s:5:\"mtype\";s:1:\"0\";s:12:\"product_pkid\";s:9:\"169779075\";s:7:\"data_id\";s:8:\"90567713\";s:7:\"user_id\";s:8:\"19182244\";s:5:\"price\";s:4:\"50.0\";s:11:\"refund_time\";s:1:\"0\";s:8:\"nickname\";s:18:\"靖哥哥的店铺\";s:13:\"refund_amount\";s:3:\"0.0\";s:8:\"order_id\";s:8:\"90567198\";s:8:\"at_price\";s:4:\"50.0\";s:12:\"refund_state\";s:1:\"0\";}}}");
        String read = read(test);


        System.out.println(read);
    }

}
