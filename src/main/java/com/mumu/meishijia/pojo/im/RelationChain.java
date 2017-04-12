package com.mumu.meishijia.pojo.im;

/**
 * 对应数据库中的relation_chain表
 * Created by Administrator on 2017/4/11.
 */
public class RelationChain {
    private int user_id;
    private int friend_id;
    private String remark;
    private String sort_letter;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSort_letter() {
        return sort_letter;
    }

    public void setSort_letter(String sort_letter) {
        this.sort_letter = sort_letter;
    }
}
