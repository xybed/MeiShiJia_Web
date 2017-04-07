package com.mumu.meishijia.dao.im;

import com.mumu.meishijia.pojo.im.Contacts;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 与im有关的dao
 * Created by Administrator on 2017/4/7.
 */
@Repository("imDao")
public interface IImDao {
    List<Contacts> queryContacts(int id);
}
