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

    // 添加线程对象到 hm 集合
    public static void addClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }

    // 根据userId返回线程
    public static ServerConnectClientThread getClientThread(String userId) {
        return hm.get(userId);
    }
}
