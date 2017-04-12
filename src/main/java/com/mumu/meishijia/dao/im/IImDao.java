package com.mumu.meishijia.dao.im;

import com.mumu.meishijia.model.im.ContactsModel;
import com.mumu.meishijia.model.im.ContactsDetailModel;
import com.mumu.meishijia.pojo.im.RelationChain;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 与im有关的dao
 * Created by Administrator on 2017/4/7.
 */
@Repository("imDao")
public interface IImDao {
    List<ContactsModel> queryContacts(int id);
    ContactsDetailModel queryContactsDetail(int userId, int friendId);
    int updateRemark(RelationChain relationChain);
}
