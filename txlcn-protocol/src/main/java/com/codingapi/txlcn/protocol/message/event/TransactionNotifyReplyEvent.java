package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.message.separate.TransactionMessage;
import lombok.Data;

/**
 * 通知回复事件
 * TC收到TM的通知响应 给TM发送通知回复事件
 *
 * @author leon
 * @date 2020/07/08
 */
@Data
public class TransactionNotifyReplyEvent extends TransactionMessage {

	private String result;

	/**
	 * 业务执行结果
	 */
	private boolean success;

	public TransactionNotifyReplyEvent(String groupId, boolean success, String applicationName) {
		this.groupId = groupId;
		this.success = success;
		this.applicationName = applicationName;
	}

}
