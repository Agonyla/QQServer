package com.hspedu.qqserver.server;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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

    ArrayList<Message> arr1 = new ArrayList<>();
    ArrayList<Message> arr2 = new ArrayList<>();
    ArrayList<Message> arr3 = new ArrayList<>();
    ArrayList<Message> arr4 = new ArrayList<>();
    ArrayList<Message> arr5 = new ArrayList<>();
    ArrayList<Message> arr6 = new ArrayList<>();

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
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

                } else if (message.getMessageType().equals(MessageType.MESSAGE_COMM_MES)) {

                    // // 根据message获取getterId，然后得到对应的线程
                    // ServerConnectClientThread clientThread = ManageClientThreads.getClientThread(message.getGetter());
                    //
                    // ObjectOutputStream oos = new ObjectOutputStream(clientThread.getSocket().getOutputStream());
                    // oos.writeObject(message);
                    int count = 0;
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()) {// 遍历
                        String s = iterator.next();
                        if (s.equals(message.getGetter()))
                            count = 1;
                    }
                    if (count == 1) {
                        ServerConnectClientThread serverConnectClientThread = ManageClientThreads.getClientThread(message.getGetter());
                        // 得到对应的socket的对象输出流，将message对象转发给指定的客户端
                        ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                        oos.writeObject(message);
                        System.out.println(message.getSendTime() + ":\n" + message.getSender() + "对" + message.getGetter() + "说" + message.getContent());
                    } else {// 优化了离线发消息功能
                        switch (message.getGetter()) {
                            case "100" -> {
                                Message message1 = new Message();
                                message1.setContent(message.getContent());
                                message1.setSender(message.getSender());
                                message1.setGetter(message.getGetter());
                                message1.setSendTime(message.getSendTime());
                                message1.setMessageType(message.getMessageType());
                                arr1.add(message1);
                                QQServer.offlineUsers.put(message.getGetter(), arr1);
                            }
                            case "200" -> {
                                Message message2 = new Message();
                                message2.setContent(message.getContent());
                                message2.setSender(message.getSender());
                                message2.setGetter(message.getGetter());
                                message2.setSendTime(message.getSendTime());
                                message2.setMessageType(message.getMessageType());
                                arr2.add(message2);
                                QQServer.offlineUsers.put(message.getGetter(), arr2);
                            }
                            case "300" -> {
                                Message message3 = new Message();
                                message3.setContent(message.getContent());
                                message3.setSender(message.getSender());
                                message3.setGetter(message.getGetter());
                                message3.setSendTime(message.getSendTime());
                                message3.setMessageType(message.getMessageType());
                                arr3.add(message3);
                                QQServer.offlineUsers.put(message.getGetter(), arr3);
                            }
                            case "至尊宝" -> {
                                Message message4 = new Message();
                                message4.setContent(message.getContent());
                                message4.setSender(message.getSender());
                                message4.setGetter(message.getGetter());
                                message4.setSendTime(message.getSendTime());
                                message4.setMessageType(message.getMessageType());
                                arr4.add(message4);
                                QQServer.offlineUsers.put(message.getGetter(), arr4);
                            }
                            case "紫霞仙子" -> {
                                Message message5 = new Message();
                                message5.setContent(message.getContent());
                                message5.setSender(message.getSender());
                                message5.setGetter(message.getGetter());
                                message5.setSendTime(message.getSendTime());
                                message5.setMessageType(message.getMessageType());
                                arr5.add(message5);
                                QQServer.offlineUsers.put(message.getGetter(), arr5);
                            }
                            case "菩提老祖" -> {
                                Message message6 = new Message();
                                message6.setContent(message.getContent());
                                message6.setSender(message.getSender());
                                message6.setGetter(message.getGetter());
                                message6.setSendTime(message.getSendTime());
                                message6.setMessageType(message.getMessageType());
                                arr5.add(message6);
                                QQServer.offlineUsers.put(message.getGetter(), arr6);
                            }
                        }
                    }


                } else if (message.getMessageType().equals(MessageType.MESSAGE_TO_ALL_MES)) {

                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();

                    Set<String> ids = hm.keySet();
                    for (String id : ids) {
                        if (!id.equals(message.getSender())) {
                            ObjectOutputStream oos = new ObjectOutputStream(hm.get(id).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }


                } else if (message.getMessageType().equals(MessageType.MESSAGE_FILE_MES)) {

                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientThreads.getClientThread(message.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(message);
                } else if (message.getMessageType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {

                    System.out.println(message.getSender() + " 退出系统");
                    // 将客户端对应的线程从集合中移除
                    // 不加sleep会抛出EOF异常
                    Thread.sleep(1);
                    ManageClientThreads.removeClientThread(message.getSender());
                    socket.close();
                    break;

                } else {
                    System.out.println("其他类型的message暂时不处理");
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
