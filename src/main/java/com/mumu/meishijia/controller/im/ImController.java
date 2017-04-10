package com.mumu.meishijia.controller.im;

import com.mumu.meishijia.controller.BaseController;
import com.mumu.meishijia.model.BaseModel;
import com.mumu.meishijia.model.im.ContactsModel;
import com.mumu.meishijia.service.im.IImService;
import lib.utils.NumberUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 有关im的controller
 * Created by Administrator on 2017/4/7.
 */
@Controller
@RequestMapping("/im")
public class ImController extends BaseController{
    @Resource
    private IImService imService;

    @RequestMapping("/contacts")
    @ResponseBody
    public BaseModel getContacts(HttpServletRequest request){
        String queryString = request.getQueryString();
        String sign = request.getParameter("sign");
        BaseModel baseModel = new BaseModel();
        if(!validateSign(queryString, sign)){
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("请求违法");
            return baseModel;
        }

        String token = request.getParameter("token");
        if(!validateToken(token)){
            baseModel.setResultType(-99);
            baseModel.setResultCode(-99);
            baseModel.setDetail("重新登录");
            baseModel.setData("重新登录");
            return baseModel;
        }

        int id = NumberUtil.parseInt(request.getParameter("id"), 0);
        List<ContactsModel> modelList = imService.queryContacts(id);
        baseModel.setResultType(0);
        baseModel.setResultCode(0);
        baseModel.setDetail("请求成功");
        baseModel.setData(modelList);
        return baseModel;
    }
}
