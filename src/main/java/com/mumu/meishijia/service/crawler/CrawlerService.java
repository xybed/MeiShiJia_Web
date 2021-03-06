package com.mumu.meishijia.service.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.mumu.meishijia.dao.crawler.ICrawlerDao;
import com.mumu.meishijia.pojo.crawler.*;
import com.mumu.meishijia.pojo.user.User;
import com.mumu.meishijia.service.BaseService;
import lib.utils.FileUtil;
import lib.utils.NumberUtil;
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

    private String savePath = "\\images\\remen\\teshuchanghe\\xiaoye\\";
    private String savePathWay = "\\images\\remen\\teshuchanghe\\xiaoye\\makeway\\";
    private String url = "http://www.xiachufang.com/category/51865/?page=";
    private int categoryId = 90;
    private String footballTeamUrl = "http://www.dongqiudi.com/data?competition=51";
    private String footballLogo = "\\images\\logo\\";
    private String footballTeamRankingUrl = "http://www.dongqiudi.com/data?competition=51";

    public void getCategory() {
        List<String> datas = new ArrayList<String>();
        String url = "http://www.xiachufang.com/category/";
        Document document;
        try {
            document = Jsoup.connect(url)
                    .timeout(10000)
                    .get();
            //一级分类
            Elements catesOne = document.select("div.page-outer")
                    .select("div.page-container")
                    .select("div.white-bg")
                    .select("div.cates-list");
            for(Element cateOne : catesOne){
                Elements catesThree = cateOne.select("div.cates-list-all").select("ul");
                for(Element cateThree : catesThree){
                    Elements lis = cateThree.select("li");
                    if(lis != null && lis.size() > 0){
                        for(Element li : lis){
                            String category = li.select("a").first().text();
                            datas.add(category);
                        }
                    }
                }
            }
            crawlerDao.insertCategory(datas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forPage(){
        String url;
        for(int i=1;i<11;i++){
            url = this.url + i;
            getDetailUrl(url);
        }
//        getDetail("http://www.xiachufang.com/recipe/78140/");
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
            for(String detailUrl : detailUrls){
                Integer result = crawlerDao.queryFoodByUrl(detailUrl);
                if(result == null){
                    //如果为空，则数据库里没有此数据
                    getDetail(detailUrl);
                }else{
                    //如果不为空，则数据库里有，插入一条数据到food_category表中
                    FoodCategory foodCategory = new FoodCategory();
                    foodCategory.setFoodId(result);
                    foodCategory.setCategoryId(categoryId);
                    try {
                        crawlerDao.insertFoodCategory(foodCategory);
                    } catch (Exception e){
                        System.out.println(result+"，"+categoryId+"数据已有");
                    }

                }
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
        food.setFoodUrl(url);
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
            foodName = StringUtil.filterUtf8mb4(foodName);
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
                summary = StringUtil.filterUtf8mb4(summary);
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
                hstr = StringUtil.filterUtf8mb4(hstr);
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
                        name = StringUtil.filterUtf8mb4(name);
                        System.out.print(""+name);
                        foodMaterialGson.setName(name);
                    }else{
                        String name = tr.select("td.name").first().text();
                        name = StringUtil.filterUtf8mb4(name);
                        System.out.print(""+name);
                        foodMaterialGson.setName(name);
                    }
                    Elements unitElm = tr.select("td.unit");
                    if(unitElm != null && unitElm.size() != 0){
                        String unit = unitElm.first().text();
                        unit = StringUtil.filterUtf8mb4(unit);
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
                    text = StringUtil.filterUtf8mb4(text);
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
                tips = StringUtil.filterUtf8mb4(tips);
                System.out.println("小贴士："+tips);
                food.setTips(tips);
            }else {
                System.out.println("小贴士：无");
                food.setTips("");
            }

            //插入数据库
            crawlerDao.insertFood(food);
            int foodId = crawlerDao.queryFoodByUrl(url);
            //补齐食物属于的分类表
            FoodCategory foodCategory = new FoodCategory();
            foodCategory.setFoodId(foodId);
            foodCategory.setCategoryId(categoryId);
            try {
                crawlerDao.insertFoodCategory(foodCategory);
            } catch (Exception e){
                System.out.println(foodId+"，"+categoryId+"数据已有");
            }


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

    /**
     * 获取球队的名字和logo
     */
    public void getFootballTeam() {
//        String html = "";
//        try {
//            WebClient webClient = new WebClient(BrowserVersion.CHROME);
//            webClient.getOptions().setJavaScriptEnabled(true);
//            webClient.getOptions().setCssEnabled(false);
//            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//            webClient.getOptions().setThrowExceptionOnScriptError(false);
//            //模拟浏览器打开一个目标网址
//            HtmlPage rootPage = webClient.getPage(footballTeamUrl);
//            Thread.sleep(2000);//主要是这个线程的等待 因为js加载也是需要时间的
//            html = rootPage.asXml();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Document document;
        try {
            document = Jsoup.connect(footballTeamUrl)
                        .timeout(10000)
                        .get();
            Element stateDetail = document.select("div#stat_detail").first();
            Element table = stateDetail.select("table").first();
            Element tbody = table.select("tbody").first();
            Elements trs = tbody.select("tr");
            List<FootballTeam> teams = new ArrayList<FootballTeam>();
            for(int i=0;i<trs.size();i++){
                if(i>1){
                    Element tr = trs.get(i);
                    Element td = tr.select("td.team").first();
                    Element img = td.select("img").first();
                    String imgUrl = img.attr("src");
                    System.out.println("下载地址："+imgUrl);
                    //下载图片
                    String wayPath = getApplicationPath() + footballLogo;
                    FileUtil.mkdir(wayPath);
                    //从url中获取图片名称
                    URI wayUri = URI.create(imgUrl);
                    String wayUriPath = wayUri.getPath();
                    wayUriPath = wayUriPath.substring(wayUriPath.lastIndexOf("/")+1, wayUriPath.length());
                    System.out.println("储存路径："+wayPath+wayUriPath);
                    //下载图片
                    FileUtil.downloadImage(imgUrl, wayPath + wayUriPath);
                    FootballTeam team = new FootballTeam();
                    team.setLeagueId(6);
                    team.setName(td.text());
                    team.setLogo(footballLogo+wayUriPath);
                    teams.add(team);
                }
            }
            crawlerDao.insertFootballTeam(teams);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFootballTeamRanking() {
        Document document;
        try {
            document = Jsoup.connect(footballTeamRankingUrl)
                    .timeout(10000)
                    .get();
            Element stateDetail = document.select("div#stat_detail").first();
            Element table = stateDetail.select("table").first();
            Element tbody = table.select("tbody").first();
            Elements trs = tbody.select("tr");
            List<FootballRanking> rankings = new ArrayList<FootballRanking>();
            for(int i=0;i<trs.size();i++){
                if(i>1){
                    FootballRanking ranking = new FootballRanking();
                    Element tr = trs.get(i);
                    Element tdTeam = tr.select("td.team").first();
                    int id = crawlerDao.queryTeamId(tdTeam.text());
                    System.out.println("球队"+tdTeam.text()+"的id是"+id);
                    ranking.setTeamId(id);
                    Elements tds = tr.select("td");
                    for(int j=0;j<tds.size();j++){
                        switch (j){
                            case 0:
                                ranking.setRanking(Integer.parseInt(tds.get(j).text()));
                                break;
                            case 2:
                                ranking.setPlays(Integer.parseInt(tds.get(j).text()));
                                break;
                            case 3:
                                ranking.setWin(Integer.parseInt(tds.get(j).text()));
                                break;
                            case 4:
                                ranking.setDraw(Integer.parseInt(tds.get(j).text()));
                                break;
                            case 5:
                                ranking.setLose(Integer.parseInt(tds.get(j).text()));
                                break;
                            case 6:
                                ranking.setGoal(Integer.parseInt(tds.get(j).text()));
                                break;
                            case 7:
                                ranking.setConceded(Integer.parseInt(tds.get(j).text()));
                                break;
                            case 8:
                                ranking.setGoalDifference(Integer.parseInt(tds.get(j).text()));
                                break;
                            case 9:
                                ranking.setPoint(Integer.parseInt(tds.get(j).text()));
                                break;
                            default:
                                break;
                        }
                    }
                    rankings.add(ranking);
                }
            }
            crawlerDao.insertFootballRanking(rankings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改数据库中的图片路径，把“\”换成“/”
     */
    public void modifyPath(int leagueId) {
        List<ModifyPath> pathList = crawlerDao.queryPath(leagueId);
        for(ModifyPath path : pathList){
            path.setLogo(path.getLogo().replace("\\", "/").substring(1));
            crawlerDao.updatePath(path);
        }
    }

    /**
     * 获取足球运动员的数据
     * 从腾讯体育上获取（腾讯唯一值得称赞的地方）
     * 先获取球员详细信息的链接
     */
    public void getFootballPlayer() {
        getAllPlayerUrl("http://soccer.stats.qq.com/team.htm?t1=178&from=xijia");
//        getPlayerDetail("http://soccer.stats.qq.com/player.htm?type=xijia&t1=37055");
    }

    private void getAllPlayerUrl(String url){
        String html = "";
        try {
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            //模拟浏览器打开一个目标网址
            HtmlPage rootPage = webClient.getPage(url);
            Thread.sleep(800);//主要是这个线程的等待 因为js加载也是需要时间的
            html = rootPage.asXml();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Document document;
        try {
            document = Jsoup.parse(html);
//                    .timeout(10000)
//                    .get();
            Element layout = document.select("div.team-main").first();
            Element fr = layout.select("div.p-sidebar").first();
            Element playerMember = fr.select("div.mod-struct").get(1);
            Element bd = playerMember.select("div.bd").first();
            Elements divs = bd.select("div.mod-format");
            for(Element div : divs){
                Elements lis = div.select("ul.player-list").first().select("li");
                for(Element li : lis){
                    String urlDetail = li.select("a").first().attr("href");
                    getPlayerDetail(urlDetail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据详情的url去获取球员具体数据
     * @param url 球员详情的url
     */
    private void getPlayerDetail(String url){
        String html = "";
        try {
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            //模拟浏览器打开一个目标网址
            HtmlPage rootPage = webClient.getPage(url);
            Thread.sleep(800);//主要是这个线程的等待 因为js加载也是需要时间的
            html = rootPage.asXml();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Document document;
        try {
            document = Jsoup.parse(html);
//                    .timeout(10000)
//                    .get();

            FootballPlayer player = new FootballPlayer();
            player.setTeam_id(1);

            Element mod1 = document.select("div.info-panel").first()
                                    .select("div[node-type=basicInfo]").first()
                                    .select("div.mod1").first();
            Element img = mod1.select("img").first();
            String downloadUrl = img.attr("src");
            System.out.println("球员头像链接："+downloadUrl);
            //下载图片
            String wayPath = getApplicationPath() + "\\images\\player\\";
            FileUtil.mkdir(wayPath);
            //从url中获取图片名称
            URI wayUri = URI.create(downloadUrl);
            String wayUriPath = wayUri.getPath();
            wayUriPath = wayUriPath.substring(wayUriPath.lastIndexOf("/")+1, wayUriPath.length());
            System.out.println("储存路径："+wayPath+wayUriPath);
            //下载图片
            FileUtil.downloadImage(downloadUrl, wayPath + wayUriPath);
            player.setAvatar("/images/player/"+wayUriPath);
            System.out.println("球员头像："+player.getAvatar());

            Element en = mod1.select("div.fl").first()
                                .select("div.mt15").first()
                                .select("p.en").first();
            player.setEng_name(en.text());
            System.out.println("球员英文名："+player.getEng_name());

            Element span = mod1.select("div.fl").first()
                    .select("ul.team-number").first()
                    .select("li:not([^style])").first()
                    .select("span").first();
            System.out.println(span.text());
            player.setNumber(NumberUtil.parseInt(span.text().substring(0, span.text().length()-1), 0));
            System.out.println("球员号码："+player.getNumber());

            Elements lis = document.select("div.info-panel").first()
                    .select("div[node-type=basicInfo]").first()
                    .select("div.mod2").first()
                    .select("div.bd").first()
                    .select("ul").first()
                    .select("li");
            for(int i=0;i<lis.size();i++){
                Element li = lis.get(i);
                switch (i){
                    case 0:
                        String name = li.text().substring(3);
                        player.setName(name);
                        System.out.println("球员中文名："+player.getName());
                        break;
                    case 1:
                        double height = NumberUtil.parseDouble(li.text().substring(3, li.text().lastIndexOf("cm")), 0);
                        player.setHeight(height);
                        System.out.println("球员身高："+player.getHeight());
                        break;
                    case 2:
                        double weight = NumberUtil.parseDouble(li.text().substring(3, li.text().lastIndexOf("kg")), 0);
                        player.setWeight(weight);
                        System.out.println("球员体重："+player.getWeight());
                        break;
                    case 3:
                        String nationality = li.text().substring(3);
                        player.setNationality(nationality);
                        System.out.println("球员国籍："+player.getNationality());
                        break;
                    case 4:
                        String birthday = li.text().substring(3);
                        player.setBirthday(birthday);
                        System.out.println("球员生日："+player.getBirthday());
                        break;
                    case 5:
                        String playPosition = li.text().substring(3);
                        player.setPlay_position(playPosition);
                        System.out.println("球员位置："+player.getPlay_position());
                        break;
                }
            }
            crawlerDao.insertFootballPlayer(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
