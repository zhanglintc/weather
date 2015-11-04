package co.zhanglintc.weather.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * Created by yanbin on 2015/11/04.
 */
public class WeatherUtils {


    /**
     * 从网络地址上取得图片
     * return a Bitmap.
     */
    public static Bitmap returnBitMap(String url) {

        URL myFileUrl = null;
        Bitmap bitmap = null;

        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);

            conn.connect();
            InputStream is = conn.getInputStream();

            bitmap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    //  获取当前系统的语言环境
    public static String getLanguge() {
        String language = Locale.getDefault().getLanguage();

        return  language;
    }

     public static InputStream getXML(String path) {

//         HttpGet getMethod = new HttpGet(path);
//         HttpClient httpClient = new DefaultHttpClient();
//
//         InputStream inputStream = null;
//         try {
//             HttpResponse response = httpClient.execute(getMethod);
//
//             inputStream = (InputStream)response.getEntity().getContent();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//         return inputStream;
//
//
//
         InputStream inputStream = null;
         try {
             URL url = new URL(path);

             if (url != null) {
                 HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                 connection.setConnectTimeout(1000000);
                 connection.setDoInput(true);
                 connection.setRequestMethod("GET");
                 int code = connection.getResponseCode();
                 if (code == 200) {
                     inputStream = connection.getInputStream();
                 }
             }
         } catch (Exception e) {
                e.printStackTrace();
         }
         return inputStream;
    }
}
