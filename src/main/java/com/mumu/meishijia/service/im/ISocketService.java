package com.mumu.meishijia.service.im;

import com.mumu.meishijia.model.im.MsgJsonModel;
import com.mumu.meishijia.pojo.user.User;

/**
 * socket的service的接口
 * Created by Administrator on 2017/4/19.
 */
public interface ISocketService {
    int insertMessage(MsgJsonModel msgJson);
    User querySendUser(int principal_id);
}
