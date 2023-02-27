package com.hspedu.qqframe;

import com.hspedu.qqserver.server.QQServer;

/**
 * @Author Agony
 * @Create 2023/2/27 20:01
 * @Version 1.0
 * 该类创建一个QQServer，启动后台的服务
 */
public class QQFrame {
    public static void main(String[] args) {
        new QQServer();
    }
}
