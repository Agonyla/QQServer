package com.hspedu.qqserver.server;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @Author Agony
 * @Create 2023/3/1 10:59
 * @Version 1.0
 */
public class SendNewsToAll implements Runnable {

    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {

        while (true) {
            System.out.println("请输入服务器要推送的新闻/消息(输入 exit 退出推送服务): ");
            String news = Utility.readString(100);
            if ("exit".equalsIgnoreCase(news)) {
                break;
            }
            // 构建消息类型，群发消息
            Message message = new Message();
            message.setMessageType(MessageType.MESSAGE_TO_ALL_MES);
            message.setSender("服务器");
            message.setContent(news);
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息给所有人: " + news);

            // 遍历当前所有的通信线程
            HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();

            for (String s : hm.keySet()) {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(hm.get(s).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
