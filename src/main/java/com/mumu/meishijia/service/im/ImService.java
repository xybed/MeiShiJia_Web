package com.mumu.meishijia.service.im;

import com.mumu.meishijia.dao.im.IImDao;
import com.mumu.meishijia.model.im.ContactsModel;
import com.mumu.meishijia.pojo.im.Contacts;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        List<Contacts> contactsList = imDao.queryContacts(id);
        List<ContactsModel> modelList = new ArrayList<ContactsModel>();
        ContactsModel model;
        for(Contacts contacts : contactsList){
            model = new ContactsModel();
            model.setId(contacts.getFriend_id());
            model.setRemark(contacts.getRemark());
            model.setAvatar(contacts.getAvatar());
            model.setSort_letter(contacts.getSort_letter());
            modelList.add(model);
        }
        return modelList;
    }
}
