package com.hspedu.qqserver.server;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
                // 根据message类型，后面做相应的处理
                if (message.getMessageType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {

                    // 客户端要在线用户列表
                    /*
                    在线用户列表显示形式
                    100 200 至尊宝 紫霞仙子
                     */
                    System.out.println(message.getSender() + " 要在线用户列表");
                    String onlineUsers = ManageClientThreads.getOnlineUsers();

                    // 返回message
                    // 构建一个message对象，返回给客户端
                    Message message2 = new Message();
                    message2.setMessageType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUsers);
                    message2.setGetter(message.getSender());

                    // 返回给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                    
                } else {
                    System.out.println("其他类型的message暂时不处理");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
