package com.codingapi.tx.spi.rpc;

import com.codingapi.tx.spi.rpc.dto.MessageDto;
import com.codingapi.tx.spi.rpc.dto.RpcCmd;
import com.codingapi.tx.spi.rpc.dto.RpcResponseState;
import com.codingapi.tx.spi.rpc.exception.RpcException;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
public interface RpcClient {

    /**
     * 发送指令不需要返回数据，需要知道返回的状态
     * @param rpcCmd    指令内容
     * @return  指令状态
     * @throws RpcException
     */
    RpcResponseState send(RpcCmd rpcCmd) throws RpcException;


    /**
     * 发送指令不需要返回数据，需要知道返回的状态
     * @param remoteKey 远程标识关键字
     * @param msg    指令内容
     * @return  指令状态
     * @throws RpcException
     */
    RpcResponseState send(String remoteKey,MessageDto msg) throws RpcException;


    /**
     * 发送请求并相应
     * @param rpcCmd   指令内容
     * @return  相应指令数据
     * @throws RpcException
     */
    MessageDto request(RpcCmd rpcCmd)throws RpcException;


    /**
     * 发送请求并相应
     * @param remoteKey   远程标识关键字
     * @param msg    指令内容
     * @return  相应指令数据
     * @throws RpcException
     */
    MessageDto request(String remoteKey,MessageDto msg)throws RpcException;


    /**
     * 获取一个远程标识关键字
     * @return
     * @throws RpcException
     */
    String loadRemoteKey()throws RpcException;


    /**
     * 获取所有的远程连接对象
     * @return  远程连接对象数组.
     */
    List<String> loadAllRemoteKey();


    /**
     * 获取模块远程标识
     * @param moduleName 模块名称
     * @return 远程标识
     */
    List<String> moduleList(String moduleName);


    /**
     * 绑定模块名称
     * @param remoteKey 远程标识
     * @param appName   应用名称
     */
    void bindAppName(String remoteKey,String appName) throws RpcException;



    /**
     * 获取模块名称
     * @param remoteKey 远程标识
     * @return   应用名称
     */
    String getAppName(String remoteKey) throws RpcException;

}
