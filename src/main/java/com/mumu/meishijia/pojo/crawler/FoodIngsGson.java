package com.mumu.meishijia.pojo.crawler;

import java.util.List;

/**
 * 食物简介的json
 * Created by Administrator on 2016/12/1.
 */
public class FoodIngsGson {
    private String ing;
    private List<FoodMaterialGson> foodMaterialGsons;

    public String getIng() {
        return ing;
    }

    public void setIng(String ing) {
        this.ing = ing;
    }

    public List<FoodMaterialGson> getFoodMaterialGsons() {
        return foodMaterialGsons;
    }

    public void setFoodMaterialGsons(List<FoodMaterialGson> foodMaterialGsons) {
        this.foodMaterialGsons = foodMaterialGsons;
    }
}
