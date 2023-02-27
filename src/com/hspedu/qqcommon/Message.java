package com.hspedu.qqcommon;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author Agony
 * @Create 2023/2/26 19:45
 * @Version 1.0
 * 表示客户端和服务端通信时的消息对象
 */
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private String sender;
    private String getter;
    private String content;
    private String sendTime;
    // 可以在接口定义消息类型
    private String messageType;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
