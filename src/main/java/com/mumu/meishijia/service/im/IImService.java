package com.mumu.meishijia.service.im;

import com.mumu.meishijia.model.im.ContactsDetailModel;
import com.mumu.meishijia.model.im.ContactsModel;
import com.mumu.meishijia.pojo.im.RelationChain;

import java.util.List;

/**
 * ImService的接口
 * Created by Administrator on 2017/4/7.
 */
public interface IImService {
    List<ContactsModel> queryContacts(int id);
    ContactsDetailModel queryContactsDetail(int user_id, int friend_id);
    int updateRemark(int userId, int friendId, String remark);
}
