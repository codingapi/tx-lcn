package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.separate.TransactionMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * 通知回复事件
 * TC收到TM的通知响应 给TM发送通知回复事件
 *
 * @author leon
 * @date 2020/07/08
 */
@Slf4j
public class TransactionNotifyReplyEvent extends TransactionMessage {

	private String result;

	/**
	 * 业务执行结果
	 */
	private boolean success;

	@Override
	public void handle(ApplicationContext springContext, Protocoler protocoler, Connection connection) throws Exception {
		super.handle(springContext, protocoler, connection);
		log.info("notify reply msg =>{} tc-client =>{} execute result =>{}",groupId,applicationName,success);
		//接收到TM的通知回复消息 不需要做处理
//		//记录tc请求通知 日志
//		this.result = "ok";
//		protocoler.sendMsg(connection.getUniqueKey(),this);
	}

}
