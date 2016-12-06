package com.mumu.meishijia.pojo.crawler;

import java.util.List;

/**
 * Created by Administrator on 2016/12/1.
 */
public class FoodStepsGson {
    private String step;
    private List<FoodMakeWayGson> foodMakeWayGsons;

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public List<FoodMakeWayGson> getFoodMakeWayGsons() {
        return foodMakeWayGsons;
    }

    public void setFoodMakeWayGsons(List<FoodMakeWayGson> foodMakeWayGsons) {
        this.foodMakeWayGsons = foodMakeWayGsons;
    }
}
