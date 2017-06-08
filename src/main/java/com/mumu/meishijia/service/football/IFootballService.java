package com.mumu.meishijia.service.football;

import com.mumu.meishijia.model.football.RankingModel;

import java.util.List;

/**
 * 足球service的接口
 * Created by Administrator on 2017/6/8.
 */
public interface IFootballService {
    List<RankingModel> getRanking(int type);
}
