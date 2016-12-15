package com.mumu.meishijia.service.food;

import com.mumu.meishijia.dao.food.IFoodDao;
import com.mumu.meishijia.model.food.RecipeModel;
import com.mumu.meishijia.model.food.RecipeSubModel;
import com.mumu.meishijia.pojo.food.CategoryIdAndName;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14.
 * 有关食物的service层
 */
@Service("foodService")
public class FoodService implements IFoodService{
    @Resource
    private IFoodDao foodDao;

    public List<RecipeModel> getRecipe() {
        //先查询level为2的食谱分类
        List<RecipeModel> recipeModels = new ArrayList<RecipeModel>();
        List<CategoryIdAndName> levelTwos = foodDao.queryCategoryByLevel(2);
        for(CategoryIdAndName levelTwo : levelTwos){
            RecipeModel recipeModel = new RecipeModel();
            recipeModel.setValue(levelTwo.getName());
            List<CategoryIdAndName> levelThrees = foodDao.queryCategoryByFid(levelTwo.getId());
            //可能二级分类没有子类
            if(levelThrees == null || levelThrees.size() <= 0){
                continue;
            }
            List<RecipeSubModel> recipeSubModels = new ArrayList<RecipeSubModel>();
            for(CategoryIdAndName levelThree : levelThrees){
                RecipeSubModel recipeSubModel = new RecipeSubModel();
                recipeSubModel.setId(levelThree.getId());
                recipeSubModel.setValue(levelThree.getName());
                recipeSubModels.add(recipeSubModel);
            }
            recipeModel.setSub(recipeSubModels);
            recipeModels.add(recipeModel);
        }
        return recipeModels;
    }
}
