package com.mumu.meishijia.dao.im;

import com.mumu.meishijia.pojo.im.MsgRecord;
import com.mumu.meishijia.pojo.im.RelationChain;
import com.mumu.meishijia.pojo.user.User;
import org.springframework.stereotype.Repository;

/**
 * socket的dao
 * Created by Administrator on 2017/4/19.
 */
@Repository("socketDao")
public interface ISocketDao {
    void insertMessage(MsgRecord msgRecord);
    User querySendUser(int principalId);
    int queryUserIdByPid(int principalId);
    String queryRemark(int userId, int friendId);
}
