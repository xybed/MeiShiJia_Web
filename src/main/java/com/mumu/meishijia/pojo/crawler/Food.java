package com.mumu.meishijia.pojo.crawler;

/**
 * Created by Administrator on 2016/11/24.
 */
public class Food {
    private String foodUrl;//食物的网上链接
    private String foodName;//食物名称
    private String foodImage;//图片地址
    private float comScore;//综合评分
    private int comments;//评论数
    private int greatCom;//极好数
    private int goodCom;//挺好数
    private int notBadCom;//一般数
    private int foodMakes;//做过这道菜的人数
    private String author;//作者
    private String summary;//简介
    private String material;//用料
    private String foodMakeWay;//做法
    private String tips;//小贴士

    public String getFoodUrl() {
        return foodUrl;
    }

    public void setFoodUrl(String foodUrl) {
        this.foodUrl = foodUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public float getComScore() {
        return comScore;
    }

    public void setComScore(float comScore) {
        this.comScore = comScore;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getGreatCom() {
        return greatCom;
    }

    public void setGreatCom(int greatCom) {
        this.greatCom = greatCom;
    }

    public int getGoodCom() {
        return goodCom;
    }

    public void setGoodCom(int goodCom) {
        this.goodCom = goodCom;
    }

    public int getNotBadCom() {
        return notBadCom;
    }

    public void setNotBadCom(int notBadCom) {
        this.notBadCom = notBadCom;
    }

    public int getFoodMakes() {
        return foodMakes;
    }

    public void setFoodMakes(int foodMakes) {
        this.foodMakes = foodMakes;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getFoodMakeWay() {
        return foodMakeWay;
    }

    public void setFoodMakeWay(String foodMakeWay) {
        this.foodMakeWay = foodMakeWay;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
