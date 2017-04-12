package com.mumu.meishijia.service.im;

import com.mumu.meishijia.constacts.Constants;
import com.mumu.meishijia.dao.im.IImDao;
import com.mumu.meishijia.model.im.ContactsDetailModel;
import com.mumu.meishijia.model.im.ContactsModel;
import com.mumu.meishijia.pojo.im.RelationChain;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 有关im的service
 * Created by Administrator on 2017/4/7.
 */
@Service("imService")
public class ImService implements IImService{

    @Resource
    private IImDao imDao;

    public List<ContactsModel> queryContacts(int id) {
        List<ContactsModel> contactsList = imDao.queryContacts(id);
        for(ContactsModel contacts : contactsList){
            contacts.setAvatar(Constants.BaseUrl + contacts.getAvatar());
        }
        return contactsList;
    }

    public ContactsDetailModel queryContactsDetail(int userId, int friendId) {
        ContactsDetailModel contactsDetailModel = imDao.queryContactsDetail(userId, friendId);
        contactsDetailModel.setAvatar(Constants.BaseUrl + contactsDetailModel.getAvatar());
        return contactsDetailModel;
    }

    public int updateRemark(int userId, int friendId, String remark) {
        RelationChain relationChain = new RelationChain();
        relationChain.setUser_id(userId);
        relationChain.setFriend_id(friendId);
        relationChain.setRemark(remark);
        return imDao.updateRemark(relationChain);
    }
}
