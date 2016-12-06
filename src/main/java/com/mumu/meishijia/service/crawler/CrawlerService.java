package com.mumu.meishijia.service.crawler;

import com.google.gson.Gson;
import com.mumu.meishijia.dao.crawler.ICrawlerDao;
import com.mumu.meishijia.pojo.crawler.*;
import com.mumu.meishijia.service.BaseService;
import lib.utils.FileUtil;
import lib.utils.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */
@Service("crawlerService")
public class CrawlerService extends BaseService implements ICrawlerService{

    @Resource
    private ICrawlerDao crawlerDao;

    private List<Food> foods = new ArrayList<Food>();
    private String savePath = "\\images\\jiachang\\";
    private String savePathWay = "\\images\\jiachang\\makeway\\";
    private int cid = 1;

    public void getCategory() {
        List<String> datas = new ArrayList<String>();
        String url = "http://www.xiachufang.com/";
        Document document;
        try {
            document = Jsoup.connect(url)
                    .timeout(10000)
                    .get();
            Elements outer = document.select("div.page-outer");
            Elements container = outer.select("div.page-container");
            Elements pure = container.select("div.pure-g");
            Element mainPanel = pure.first();
            Elements pur = mainPanel.select("div.pure-g");
            Element leftPanel = pur.first();
            Elements ul = leftPanel.select("ul");
            Elements lis = ul.select("li");
            for(int i=0;i<lis.size();i++){
                Element li = lis.get(i);
                Element span = li.select("span.cat-name").first();
                if(span != null && !StringUtil.isEmpty(span.text())){
                    datas.add(span.text());
                }
            }
            crawlerDao.insertCategory(datas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forPage(){
        String url;
        for(int i=6;i<11;i++){
            url = "http://www.xiachufang.com/category/40076/?page="+i;
            getDetailUrl(url);
        }
//        getDetail("http://www.xiachufang.com/recipe/182788/");
    }

    /**
     * 获取食物的详情链接
     */
    private void getDetailUrl(String url){
        List<String> detailUrls = new ArrayList<String>();
        Document document;
        try {
            document = Jsoup.connect(url)
                    .timeout(10000)
                    .get();
            Elements outer = document.select("div.page-outer");
            Elements container = outer.select("div.page-container");
            Elements pure = container.select("div.pure-g");
            Element mainPanel = pure.first();
            Elements block = mainPanel.select("div.white-bg");
            Elements pur = block.select("div.pure-g");
            Elements pureu = pur.select("div.pure-u-3-4");
            Elements normal = pureu.select("div.normal-recipe-list");
            Elements ul = normal.select("ul");
            Elements lis = ul.select("li");
            for(Element li : lis){
                String detailUrl = li.select("div.recipe-140-horizontal")
                        .select("div.cover")
                        .select("a")
                        .attr("abs:href");
                detailUrls.add(detailUrl);
            }
//            for(int i=0;i<19;i++){
//                detailUrls.remove(0);
//            }
            for(String detailUrl : detailUrls){
                getDetail(detailUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据详情链接获取主要数据
     * @param url 详情链接
     */
    private void getDetail(String url){
        System.out.println("访问详情链接："+url);
        Food food = new Food();
        Document document;
        try {
            document = Jsoup.connect(url)
                    .timeout(10000)
                    .get();
            Element divMain = document.select("div.page-outer")
                    .select("div.page-container")
                    .select("div.pure-g")
                    .select("div.pure-u-2-3")
                    .first();

            //拿食物名字
            String foodName = divMain.select("h1").first().text();
            food.setFoodName(foodName);
            System.out.println("食物名字："+foodName);

            //拿食物的图片链接
            String foodImgUrl = divMain.select("div.block").select("div.cover").select("img").first().attr("src");
            System.out.println("食物图片链接："+foodImgUrl);
            //存放路径
            String filePath = getApplicationPath() + savePath;
            FileUtil.mkdir(filePath);
            //从url中获取图片名称
            URI uri = URI.create(foodImgUrl);
            String uriPath = uri.getPath().substring(1);
            if(uriPath.contains("@")){
                uriPath = uriPath.substring(0, uriPath.indexOf("@"));
            }
            //下载图片
            FileUtil.downloadImage(foodImgUrl, filePath + uriPath);
            food.setFoodImage(savePath+uriPath);
            System.out.println("存储路径："+savePath+uriPath);

            //拿评分、做过人数等数据
            Element scoreContainer = divMain.select("div.block").select("div.container").select("div.stats").first();
            //评分div
            Elements divScoreElm = scoreContainer.select("div.score");
            if(divScoreElm != null && divScoreElm.size() != 0){
                Element divScore = divScoreElm.first();
                //综合评分
                String comScore = divScore.select("div.overview").select("span").first().text();
                food.setComScore(Float.parseFloat(comScore));
                System.out.println("综合评分："+comScore);
                //评论数
                String comments = divScore.select("div.detail").select("div.title").select("strong").first().text();
                System.out.println("评论数："+comments);
                food.setComments(Integer.parseInt(comments));
                //极好、挺好、一般
                Elements trsCom = divScore.select("div.detail").select("table").select("tbody").select("tr");
                String greatCom = trsCom.get(0).select("td.num").first().text();
                System.out.println("极好数："+greatCom);
                food.setGreatCom(Integer.parseInt(greatCom));
                String goodCom = trsCom.get(1).select("td.num").first().text();
                System.out.println("挺好数："+goodCom);
                food.setGoodCom(Integer.parseInt(goodCom));
                String notBadCom = trsCom.get(2).select("td.num").first().text();
                System.out.println("一般数："+notBadCom);
                food.setNotBadCom(Integer.parseInt(notBadCom));
            }else {
                food.setComScore(0);
                food.setComments(0);
                food.setGreatCom(0);
                food.setGoodCom(0);
                food.setNotBadCom(0);
            }
            //做过这道菜的人数
            String foodMakes = scoreContainer.select("div.cooked").select("div.overview").select("span").first().text();
            System.out.println("做过人数："+foodMakes);
            food.setFoodMakes(Integer.parseInt(foodMakes));

            //简介、作者、用料、做法
            Element divBlock = divMain.select("div.block").first();
            //简介
            Elements summaryElm = divBlock.select("div.desc");
            if(summaryElm != null && summaryElm.size() != 0){
                String summary = summaryElm.first().text();
                System.out.println("简介："+summary);
                food.setSummary(summary);
            }else{
                System.out.println("简介：无");
                food.setSummary("");
            }

            //作者
            String author = divBlock.select("div.author").select("a").select("span").first().text();
            System.out.println("作者："+author);
            food.setAuthor(author);
            //用料(可能会有很多)
            //h标签的值，包括用料和做法
            Elements hs = divBlock.select("h2");
            List<String> hsStr = new ArrayList<String>();
            for(Element h : hs){
                String hstr = h.text().replaceAll("\u00A0", "");
                hsStr.add(hstr);
                System.out.println(hstr);
            }
            hsStr.remove(0);//去除简介
            Gson gson = new Gson();
            List<FoodIngsGson> foodIngsGsons = new ArrayList<FoodIngsGson>();
            Elements ings = divBlock.select("div.ings");
            for(Element ing : ings){
                Elements trsMaterial = ing.select("table").select("tbody").select("tr");
                FoodIngsGson foodIngsGson = new FoodIngsGson();
                List<FoodMaterialGson> foodMaterialGsons = new ArrayList<FoodMaterialGson>();
                for(Element tr : trsMaterial){
                    FoodMaterialGson foodMaterialGson = new FoodMaterialGson();
                    Elements tdNameElm = tr.select("td.name").select("a");
                    if(tdNameElm != null && tdNameElm.size() != 0){
                        String name = tdNameElm.first().text();
                        System.out.print(""+name);
                        foodMaterialGson.setName(name);
                    }else{
                        String name = tr.select("td.name").first().text();
                        System.out.print(""+name);
                        foodMaterialGson.setName(name);
                    }
                    Elements unitElm = tr.select("td.unit");
                    if(unitElm != null && unitElm.size() != 0){
                        String unit = unitElm.first().text();
                        System.out.println(""+unit);
                        foodMaterialGson.setUnit(unit);
                    }else {
                        foodMaterialGson.setUnit(null);
                    }
                    foodMaterialGsons.add(foodMaterialGson);
                }
                foodIngsGson.setFoodMaterialGsons(foodMaterialGsons);
                foodIngsGsons.add(foodIngsGson);
            }
            for(FoodIngsGson foodIngsGson : foodIngsGsons){
                foodIngsGson.setIng(hsStr.get(0));
                hsStr.remove(0);
            }
            food.setMaterial(gson.toJson(foodIngsGsons));
            //做法
            List<FoodStepsGson> foodStepsGsons = new ArrayList<FoodStepsGson>();
            Elements steps = divBlock.select("div.steps");
            for(Element step :steps){
                FoodStepsGson foodStepsGson = new FoodStepsGson();
                List<FoodMakeWayGson> foodMakeWayGsons = new ArrayList<FoodMakeWayGson>();
                Elements lis = step.select("ol").select("li");
                for(Element li : lis){
                    FoodMakeWayGson foodMakeWayGson = new FoodMakeWayGson();
                    String text = li.select("p").first().text();
//                    if(text.contains("准备卤水配料")){
//                        text = text.substring(0, text.length() - 2);
//                    }else if(text.contains("出锅前")){
//                        text = text.substring(0, text.indexOf("出锅前")-4)+text.substring(text.indexOf("出锅前"), text.length());
//                    }
                    System.out.print(text);
                    foodMakeWayGson.setText(text);
                    //做法的图片的url
                    Elements imgElm = li.select("img");
                    String imgUrl = null;
                    if(imgElm != null && imgElm.size() != 0){
                        imgUrl = imgElm.first().attr("src");
                    }
                    if(imgUrl != null){
                        System.out.println(""+imgUrl);
                        String wayPath = getApplicationPath() + savePathWay;
                        FileUtil.mkdir(wayPath);
                        //从url中获取图片名称
                        URI wayUri = URI.create(imgUrl);
                        String wayUriPath = wayUri.getPath().substring(1);
                        if(wayUriPath.contains("@")){
                            wayUriPath = wayUriPath.substring(0, wayUriPath.indexOf("@"));
                        }
                        //下载图片
                        FileUtil.downloadImage(imgUrl, wayPath + wayUriPath);
                        foodMakeWayGson.setImg(savePathWay+wayUriPath);
                    }else {
                        foodMakeWayGson.setImg(null);
                    }
                    foodMakeWayGsons.add(foodMakeWayGson);
                }
                foodStepsGson.setFoodMakeWayGsons(foodMakeWayGsons);
                foodStepsGsons.add(foodStepsGson);
            }
            for(FoodStepsGson foodStepsGson : foodStepsGsons){
                foodStepsGson.setStep(hsStr.get(0));
                hsStr.remove(0);
            }
            food.setFoodMakeWay(gson.toJson(foodStepsGsons));

            //小贴士
            Elements tipsElm = divBlock.select("div.tip-container");
            if(tipsElm != null && tipsElm.size() != 0){
                String tips = tipsElm.select("div.tip").first().text();
                System.out.println("小贴士："+tips);
                food.setTips(tips);
            }else {
                System.out.println("小贴士：无");
                food.setTips("");
            }

            //补齐其他数据
            food.setCategory(cid);//烘焙的cid为10

            //插入数据库
            crawlerDao.insertFood(food);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAllUrl(){
//        String url = "http://www.xiachufang.com/category/51761/";
//        Document document = null;
//        try {
//            document = Jsoup.connect(url)
//                    .timeout(10000)
//                    .get();
//            Elements links = document.select("a[href]");
//            Elements media = document.select("[src]");
//            Elements imports = document.select("link[href]");
//
//            for(Element src : media){
//                System.out.println(src.tagName()+"     "+src.attr("abs:src"));
//            }
//            for(Element link : imports){
//                System.out.println(link.tagName()+"     "+link.attr("abs:href"));
//            }
//            for(Element link : links){
//                System.out.println(link.tagName()+"     "+link.attr("abs:href"));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        String img = "http://s2.cdn.xiachufang.com/1535fe9087b611e6b87c0242ac110003_500w_666h.jpg?imageView2/2/w/620/interlace/1/q/90";
        String filePath = getApplicationPath() + "\\images";
        FileUtil.mkdir(filePath);
        FileUtil.downloadImage(img, filePath + "\\test.jpg");
    }
}
