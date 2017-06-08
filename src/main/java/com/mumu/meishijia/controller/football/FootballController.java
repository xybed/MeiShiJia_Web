package com.mumu.meishijia.controller.football;

import com.mumu.meishijia.controller.BaseController;
import com.mumu.meishijia.model.BaseModel;
import com.mumu.meishijia.model.football.RankingModel;
import com.mumu.meishijia.service.football.FootballService;
import lib.utils.NumberUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 有关足球的controller
 * Created by Administrator on 2017/6/8.
 */
@Controller
@RequestMapping("/football")
public class FootballController extends BaseController{

    @Resource
    private FootballService footballService;

    @RequestMapping("/ranking")
    @ResponseBody
    public BaseModel getRanking(HttpServletRequest request){
        String queryString = request.getQueryString();
        String sign = request.getParameter("sign");
        BaseModel baseModel = new BaseModel();
        if(!validateSign(queryString, sign)){
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("请求违法");
            return baseModel;
        }

        int type = NumberUtil.parseInt(request.getParameter("type"), 0);
        if(type<1 || type>6){
            baseModel.setResultType(-1);
            baseModel.setResultCode(-1);
            baseModel.setDetail("请求违法");
            return baseModel;
        }
        List<RankingModel> rankingList = footballService.getRanking(type);
        baseModel.setResultType(0);
        baseModel.setResultCode(0);
        baseModel.setDetail("请求成功");
        baseModel.setData(rankingList);
        return baseModel;
    }
}
