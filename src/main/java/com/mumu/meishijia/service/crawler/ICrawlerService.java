package com.mumu.meishijia.service.crawler;

/**
 * Created by 7mu on 2016/11/23.
 */
public interface ICrawlerService {
    void getCategory();
    void forPage();
    void getAllUrl();
    void getFootballTeam();
    void getFootballTeamRanking();
    void modifyPath(int leagueId);
    void getFootballPlayer();
}
