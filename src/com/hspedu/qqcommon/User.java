package com.hspedu.qqcommon;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author Agony
 * @Create 2023/2/26 19:45
 * @Version 1.0
 * 表示一个用户/客户信息
 */
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID/用户名
     */
    private String userId;
    /**
     * 用户密码
     */
    private String passwd;

    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }
}
