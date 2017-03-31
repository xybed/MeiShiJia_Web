import com.google.gson.Gson;
import com.mumu.meishijia.controller.BaseController;
import com.mumu.meishijia.pojo.crawler.FoodMaterialGson;
import com.mumu.meishijia.service.user.UserService;
import lib.utils.DateUtil;
import lib.utils.MD5Util;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */
public class TestMethod {
    @Test
    public void test(){
        String foodUrl = "http://s2.cdn.xiachufang.com/0d756d64873711e6a9a10242ac110002_600w_800h.jpg?imageView2/2/w/620/interlace/1/q/90";
        URI uri = URI.create(foodUrl);
        System.out.println(uri.getHost());
        System.out.println(uri.getPath());
        System.out.println(uri.getQuery());
    }

    @Test
    public void testGson(){
        List<FoodMaterialGson> foodMaterialGsons = new ArrayList<FoodMaterialGson>();
        for(int i=0;i<3;i++){
            FoodMaterialGson foodMaterialGson = new FoodMaterialGson();
            String name = "低粉"+i;
            foodMaterialGson.setName(name);
            String unit = "100克"+i;
            foodMaterialGson.setUnit(unit);
            foodMaterialGsons.add(foodMaterialGson);
        }
        Gson gson = new Gson();
        System.out.print(gson.toJson(foodMaterialGsons));
    }

    @Test
    public void testChar(){
        String str = "！！";
        str = str.replaceAll("[^\\u0020-\\u9FA5]", "");
        System.out.println(str);
    }

    @Test
    public void canModifyTest(){
        String s1 = "MeiShiJiabirthday=&city=&email=&id=1&nickname=大木神&platform=android&real_name=mumu&token=e4ac899983b7f81c0ac8d3304320dbed&ver=1MeiShiJia";
        String s2 = "MeiShiJiabirthday=&city=&email=&id=1&nickname=%E5%A4%A7%E6%9C%A8%E7%A5%9E&platform=android&real_name=mumu&token=e4ac899983b7f81c0ac8d3304320dbed&ver=1MeiShiJia";
        System.out.println(MD5Util.MD5(s1));
        try {
            System.out.println(MD5Util.MD5(URLDecoder.decode(s2, "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
