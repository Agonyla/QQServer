package com.hspedu.qqserver.server;

import java.util.HashMap;

/**
 * @Author Agony
 * @Create 2023/2/27 19:44
 * @Version 1.0
 * 该类用于管理和客户端通信的线程
 */
public class ManageClientThreads {

    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    // 添加线程对象到 hm 集合
    public static void addClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }

    // 根据userId返回线程
    public static ServerConnectClientThread getClientThread(String userId) {
        return hm.get(userId);
    }

    // 从集合中移除某个线程对象
    public static void removeClientThread(String userId) {
        hm.remove(userId);
    }

    // 编写方法 返回在线用户列表
    // 在线用户列表显示形式
    // 100 200 至尊宝 紫霞仙子
    public static String getOnlineUsers() {

        String onlineUserList = "";

        for (String s : hm.keySet()) {
            onlineUserList += s + " ";
        }
        return onlineUserList;
    }
}
