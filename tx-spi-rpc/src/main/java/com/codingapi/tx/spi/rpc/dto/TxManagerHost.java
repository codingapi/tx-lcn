package com.codingapi.tx.spi.rpc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxManagerHost {

    private String host;

    private int port;


    public static List<TxManagerHost> parserList(List<String> managerHost) {
        List<TxManagerHost> list = new ArrayList<>();
        for(String host:managerHost){
            String [] array = host.split(":");
            TxManagerHost manager = new TxManagerHost(array[0],Integer.parseInt(array[1]));
            list.add(manager);
        }
        return list;
    }
}
