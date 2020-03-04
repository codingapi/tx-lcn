/**
 * @author lorne
 * @date 2020/3/4
 * @description
 *
 * message 分为两种
 * 1. 接受相同的业务.[继承Message的消息]
 *   接受相同的业务是指发送的消息与处理消息都在相同的业务模块下。
 * 2. 接受不同的业务.[继承AbsMessage的消息]
 *   发送数据与处理数据来自于不同的模块，a->b,a只管发送，b负责处理。分离的业务要求双方的消息类包路径一致，传递的数据一致，可序列化。
 */
package com.codingapi.txlcn.protocol.message;