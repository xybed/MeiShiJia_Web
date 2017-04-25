package com.mumu.meishijia.service.im;

import com.mumu.meishijia.dao.im.ISocketDao;
import com.mumu.meishijia.model.im.MsgJsonModel;
import com.mumu.meishijia.pojo.im.MsgRecord;
import com.mumu.meishijia.pojo.im.RelationChain;
import com.mumu.meishijia.pojo.user.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * socket的service
 * Created by Administrator on 2017/4/19.
 */
@Service("socketService")
public class SocketService implements ISocketService{

    @Resource
    private ISocketDao socketDao;

    /**
     * msgJson转成MsgRecord，存进数据库
     * @param msgJson json的java对象
     */
    public int insertMessage(MsgJsonModel msgJson) {
        MsgRecord msgRecord = new MsgRecord();
        msgRecord.setFrom_id(msgJson.getData().getFrom_id());
        msgRecord.setTo_id(msgJson.getData().getTo_id());
        msgRecord.setMsg_type(msgJson.getMsg_type());
        msgRecord.setMsg_time(msgJson.getData().getTime());
        msgRecord.setMsg_content(msgJson.getData().getMsg_content());
        //TODO 要修改，这里暂时为1
        msgRecord.setSystem_attach(1);
        socketDao.insertMessage(msgRecord);
        return msgRecord.getId();
    }

    public User querySendUser(int principalId) {
        return socketDao.querySendUser(principalId);
    }

    public int queryUserIdByPid(int principalId) {
        return socketDao.queryUserIdByPid(principalId);
    }

    public String queryRemark(int userId, int friendId) {
        return socketDao.queryRemark(userId, friendId);
    }
}
