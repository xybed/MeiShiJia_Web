package lib.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by 7mu on 2016/11/24.
 */
public class FileUtil {

    /**
     * 下载图片
     * @param urlString 图片的url
     * @param filePath 保存路径
     */
    public static void downloadImage(String urlString, String filePath){
        File file = new File(filePath);
        if(file.exists())
            return;
        // 输入流
        InputStream is = null;
        // 输出的文件流
        OutputStream os = null;
        try {
            // 构造URL
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            os = new FileOutputStream(filePath);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                if(is != null)
                    is.close();
                if(os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断文件夹是否存在，并创建
     * @param filePath 文件夹路径
     */
    public static void mkdir(String filePath){
        File file = new File(filePath);
        if(!file.exists() && !file.isDirectory()){
            file.mkdirs();
        }
    }
}
