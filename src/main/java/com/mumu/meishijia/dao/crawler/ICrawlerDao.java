package com.mumu.meishijia.dao.crawler;

import com.mumu.meishijia.pojo.crawler.Food;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */
@Repository("crawlerDao")
public interface ICrawlerDao {
    void insertCategory(List<String> names);
    void insertFood(Food food);
}
