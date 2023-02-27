package com.hspedu.qqserver.server;

import com.hspedu.qqcommon.Message;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @Author Agony
 * @Create 2023/2/27 19:38
 * @Version 1.0
 * 该类对应的对象和某个客户端保持通信
 */
public class ServerConnectClientThread extends Thread {

    private Socket socket;
    // 连接到服务端的用户id
    private String userId;

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    // 可以发送和接受消息
    @Override
    public void run() {

        while (true) {
            System.out.println("服务器端和客户端保持通信," + userId + "读取数据...");
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
