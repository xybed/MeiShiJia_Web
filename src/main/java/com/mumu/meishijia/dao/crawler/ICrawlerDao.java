package com.mumu.meishijia.dao.crawler;

import com.mumu.meishijia.pojo.crawler.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */
@Repository("crawlerDao")
public interface ICrawlerDao {
    void insertCategory(List<String> names);
    void insertFood(Food food);
    Integer queryFoodByUrl(String foodUrl);
    void insertFoodCategory(FoodCategory foodCategory);
    void insertFootballTeam(List<FootballTeam> teams);
    int queryTeamId(String name);
    void insertFootballRanking(List<FootballRanking> rankings);
    List<ModifyPath> queryPath(int leagueId);
    void updatePath(ModifyPath modifyPath);
    void insertFootballPlayer(FootballPlayer player);
}
