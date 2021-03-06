package com.mumu.meishijia.controller.crawler;

import com.mumu.meishijia.controller.BaseController;
import com.mumu.meishijia.service.crawler.ICrawlerService;
import lib.utils.NumberUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by 7mu on 2016/11/23.
 */
@Controller
@RequestMapping("/crawler")
public class CrawlerController extends BaseController{
    @Resource
    private ICrawlerService crawlerService;

    @RequestMapping("/category")
    @ResponseBody
    public void getCategory(){
        crawlerService.getCategory();
    }

    /**
     * 获取食物详情的url
     */
    @RequestMapping("/detailUrl")
    @ResponseBody
    public void getDetailUrl(){
        crawlerService.forPage();
    }

    @RequestMapping("/url")
    @ResponseBody
    public void getAllUrl(){
        crawlerService.getAllUrl();
    }

    @RequestMapping("/footballTeam")
    @ResponseBody
    public void getFootballTeam(){
        crawlerService.getFootballTeam();
    }

    @RequestMapping("/footballTeamRanking")
    @ResponseBody
    public void getFootballTeamRanking(){
        crawlerService.getFootballTeamRanking();
    }

    @RequestMapping("/modifyPath")
    @ResponseBody
    public void modifyPath(HttpServletRequest request){
        int leagueId = NumberUtil.parseInt(request.getParameter("id"), 0);
        crawlerService.modifyPath(leagueId);
    }

    @RequestMapping("/footballPlayer")
    @ResponseBody
    public void getFootballPlayer(){
        crawlerService.getFootballPlayer();
    }
}
