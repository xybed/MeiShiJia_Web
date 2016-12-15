package com.mumu.meishijia.controller.food;

import com.mumu.meishijia.controller.BaseController;
import com.mumu.meishijia.model.BaseModel;
import com.mumu.meishijia.model.food.RecipeModel;
import com.mumu.meishijia.service.food.IFoodService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by 7mu on 2016/12/14.
 * 有关食物的api
 */
@Controller
@RequestMapping("/food")
public class FoodController extends BaseController{

    @Resource
    private IFoodService foodService;

    @RequestMapping("/recipe")
    @ResponseBody
    public BaseModel getRecipe(HttpServletRequest request){
        //验证签名sign，保证请求的安全性
//        String queryString = request.getQueryString();
//        String sign = request.getParameter("sign");
        BaseModel baseModel = new BaseModel();
//        if(!validateSign(queryString, sign)){
//            baseModel.setResultType(-1);
//            baseModel.setResultCode(-1);
//            baseModel.setDetail("请求违法");
//            return baseModel;
//        }
        List<RecipeModel> recipeModels = foodService.getRecipe();
        baseModel.setResultType(0);
        baseModel.setResultCode(0);
        baseModel.setData(recipeModels);
        baseModel.setDetail("请求成功");
        return baseModel;
    }
}
