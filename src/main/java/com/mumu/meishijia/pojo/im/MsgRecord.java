package com.mumu.meishijia.pojo.im;

/**
 * 对应数据库的msg_record消息记录表
 * Created by Administrator on 2017/4/19.
 */
public class MsgRecord {
    private int id;
    private int from_id;
    private int to_id;
    private int msg_type;
    private long msg_time;
    private String msg_content;
    private int system_attach;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public int getTo_id() {
        return to_id;
    }

    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public long getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(long msg_time) {
        this.msg_time = msg_time;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public int getSystem_attach() {
        return system_attach;
    }

    public void setSystem_attach(int system_attach) {
        this.system_attach = system_attach;
    }
}
