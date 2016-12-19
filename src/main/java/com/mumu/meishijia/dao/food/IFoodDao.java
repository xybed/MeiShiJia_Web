package com.mumu.meishijia.dao.food;

import com.mumu.meishijia.pojo.food.CategoryIdAndName;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 */
@Repository("foodDao")
public interface IFoodDao {
    List<CategoryIdAndName> queryCategoryByLevel(int level);
    List<CategoryIdAndName> queryCategoryByFid(int fid);
    List<CategoryIdAndName> queryRecipe(int level);
}
