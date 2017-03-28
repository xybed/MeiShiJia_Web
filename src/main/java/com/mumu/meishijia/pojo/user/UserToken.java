package com.mumu.meishijia.pojo.user;

/**
 * 对应表user_token的pojo
 * Created by Administrator on 2017/3/28.
 */
public class UserToken {
    private String username;
    private String token;
    private String deadline;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
