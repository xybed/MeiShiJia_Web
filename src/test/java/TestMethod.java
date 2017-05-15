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
        System.out.println(DateUtil.getChatTimeStr(System.currentTimeMillis()));
    }
}
