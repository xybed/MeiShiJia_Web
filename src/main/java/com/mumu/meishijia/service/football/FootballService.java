package com.mumu.meishijia.service.football;

import com.mumu.meishijia.constacts.Constants;
import com.mumu.meishijia.dao.football.IFootballDao;
import com.mumu.meishijia.model.football.RankingModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 有关足球的service
 * Created by Administrator on 2017/6/8.
 */
@Service("footballService")
public class FootballService implements IFootballService{

    @Resource
    private IFootballDao footballDao;

    public List<RankingModel> getRanking(int type) {
        List<RankingModel> rankingList = footballDao.queryRanking(type);
        for(RankingModel ranking : rankingList){
            ranking.setLogo(Constants.BaseUrl + ranking.getLogo());
        }
        return rankingList;
    }
}
