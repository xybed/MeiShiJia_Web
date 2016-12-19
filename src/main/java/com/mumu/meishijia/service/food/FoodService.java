package com.mumu.meishijia.service.food;

import com.mumu.meishijia.dao.food.IFoodDao;
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

    public List<RecipeSubModel> getRecipe() {
        List<CategoryIdAndName> categoryIdAndNames = foodDao.queryRecipe(3);
        List<RecipeSubModel> recipeSubModels = new ArrayList<RecipeSubModel>();
        for(CategoryIdAndName categoryIdAndName : categoryIdAndNames){
            RecipeSubModel recipeSubModel = new RecipeSubModel();
            recipeSubModel.setId(categoryIdAndName.getId());
            recipeSubModel.setValue(categoryIdAndName.getName());
            recipeSubModels.add(recipeSubModel);
        }
        return recipeSubModels;
    }
}
