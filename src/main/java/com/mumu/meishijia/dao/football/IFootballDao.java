package com.mumu.meishijia.dao.football;

import com.mumu.meishijia.model.football.RankingModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 足球的dao
 * Created by Administrator on 2017/6/8.
 */
@Repository("footballDao")
public interface IFootballDao {
    List<RankingModel> queryRanking(int leagueId);
}
