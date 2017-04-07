package com.mumu.meishijia.model.im;

/**
 * 联系人的model
 * Created by Administrator on 2017/4/7.
 */
public class ContactsModel {
    private int id;
    private String remark;
    private String avatar;
    private String sort_letter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSort_letter() {
        return sort_letter;
    }

    public void setSort_letter(String sort_letter) {
        this.sort_letter = sort_letter;
    }
}
