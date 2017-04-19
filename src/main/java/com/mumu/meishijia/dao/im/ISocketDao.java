package com.mumu.meishijia.dao.im;

import com.mumu.meishijia.pojo.im.MsgRecord;
import org.springframework.stereotype.Repository;

/**
 * socket的dao
 * Created by Administrator on 2017/4/19.
 */
@Repository("socketDao")
public interface ISocketDao {
    void insertMessage(MsgRecord msgRecord);
}
