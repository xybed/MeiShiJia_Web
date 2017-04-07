package com.mumu.meishijia.service.im;

import com.mumu.meishijia.model.im.ContactsModel;

import java.util.List;

/**
 * ImService的接口
 * Created by Administrator on 2017/4/7.
 */
public interface IImService {
    List<ContactsModel> queryContacts(int id);
}
