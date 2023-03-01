package com.hspedu.qqserver.server;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Agony
 * @Create 2023/2/27 19:25
 * @Version 1.0
 * 这是服务器, 在监听9999, 等待客户端的连接, 并保持通信
 */
public class QQServer {

    private ServerSocket serverSocket = null;

    // 创建一个集合，存放多个用户，如果是这些用户登录，就认为是合法的
    // 可以使用 ConcurrentHashMap，可以处理并发的集合.没有线程安全问题
    // ConcurrentHashMap 处理的线程安全，即线程同步处理，在多线程情况下是安全的
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();

    // 发送离线消息
    public static ConcurrentHashMap<String, ArrayList<Message>> offlineUsers = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, Message> offlineMessage = new ConcurrentHashMap<>();

    // 在静态代码块初始化 validUsers
    static {
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("至尊宝", new User("至尊宝", "123456"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUsers.put("菩提老祖", new User("菩提老祖", "123456"));
    }

    private boolean checkUser(String userId, String password) {

        // Set<Map.Entry<String, User>> entries = validUsers.entrySet();
        // for (Map.Entry<String, User> entry : entries) {
        //     if (entry.getKey().equals(userId) && entry.getValue().equals(password))
        //         return true;
        // }
        // return false;

        User user = validUsers.get(userId);
        // 用户名不存在
        if (user == null) {
            return false;
        }

        if (!user.getPasswd().equals(password)) {
            return false;
        }
        return true;
    }

    public QQServer() {


        try {
            System.out.println("服务器在9999端口监听....");
            // 启动推送服务
            new Thread(new SendNewsToAll()).start();
            serverSocket = new ServerSocket(9999);
            // 当和某个客户端连接后，会继续监听，因此用while
            while (true) {

                // 读取对象
                // 如果没有客户端监听就会阻塞在这
                Socket socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                User user = (User) ois.readObject();

                // 输出流回复message
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                // 创建一个Message对象,准备回复客户端
                Message message = new Message();
                // 登录成功
                // 验证用户
                if (checkUser(user.getUserId(), user.getPasswd())) {

                    // 设置消息表示登录成功
                    message.setMessageType(MessageType.MESSAGE_LOGIN_SUCCEED);

                    // 将message对象回复
                    oos.writeObject(message);

                    // 创建一个线程，和客户端把持通信，该线程需要持有socket对象
                    ServerConnectClientThread scct = new ServerConnectClientThread(socket, user.getUserId());
                    scct.start();

                    // 把线程对象放入到集合中管理
                    ManageClientThreads.addClientThread(user.getUserId(), scct);

                    for (String s : offlineUsers.keySet()) {
                        if (user.getUserId().equals(s)) {
                            for (Message message1 : offlineUsers.get(s)) {
                                ObjectOutputStream oos1 = new ObjectOutputStream(socket.getOutputStream());
                                oos1.writeObject(message1);
                            }
                            offlineUsers.remove(s);
                        }
                    }

                    for (String s : offlineMessage.keySet()) {
                        if (user.getUserId().equals(s)) {
                            ObjectOutputStream oos2 = new ObjectOutputStream(socket.getOutputStream());
                            oos2.writeObject(offlineMessage.get(s));
                            offlineMessage.remove(s);
                        }
                    }

                } else {
                    System.out.println("用户id = " + user.getUserId() + "密码  = " + user.getPasswd() + " 验证失败");
                    message.setMessageType(MessageType.MESSAGE_LOGIN_FAILED);
                    oos.writeObject(message);
                    socket.close();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 如果服务端推出了while，说明服务端不再监听，因此需要关闭serversocket
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
