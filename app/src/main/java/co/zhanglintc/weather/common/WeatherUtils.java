package co.zhanglintc.weather.common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by yanbin on 2015/11/04.
 */
public class WeatherUtils {

    // Global static variable
    final public static boolean DO_TRANSLATE = true;
    final public static boolean DO_NOT_TRANSLATE = false;
    final public static boolean LONG_FORMAT = false;
    final public static boolean SHORT_FORMAT = true;

    /**
     * 名字我没改, 沿用的urlRun
     * 但是实际我觉得只是一段测试代码
     * 随时可以删掉, 或者最好改个名字
     *
     * @param activity
     */
    public static void urlRun(Activity activity) {
        // To yanbin: Looper无用代码 ?
        // Looper.prepare();

        // TEST
        Map<String, Object> cityMap = new HashMap();

        try {
            InputStream inputStream = activity.getAssets().open("language.xml");
            cityMap = PullXMLTools.parseXML(inputStream, "UTF-8", WeatherUtils.getLanguge());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("xml", "city: " + cityMap.get("city"));

        // To yanbin: Looper无用代码 ?
        // Looper.loop();
    }

    public static String convertStreamToString(InputStream is) {
        /*
          * To convert the InputStream to String we use the BufferedReader.readLine()
          * method. We iterate until the BufferedReader return null which means
          * there's no more data to read. Each line will appended to a StringBuilder
          * and returned as String.
          */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

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

        return language;
    }

    public static InputStream getXML(String path) {

//         HttpGet getMethod = new HttpGet(path);
//         HttpClient httpClient = new DefaultHttpClient();
//
//         InputStream inputStream = null;
//         try {
//             HttpResponse response = httpClient.execute(getMethod);
//
//             inputStream = response.getEntity().getContent();
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//         return inputStream;
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

    public static String getWeek(int day, boolean shorten) {
        long offset = 86400 * 1000;

        String format = shorten ? "EEE" : "EEEE";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curWeek = new Date(System.currentTimeMillis() + offset * day);

        return formatter.format(curWeek);
    }

    public static String getSysDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date curDate = new Date(System.currentTimeMillis());

        return formatter.format(curDate);
    }

    public static String getSysTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curTime = new Date(System.currentTimeMillis());

        return formatter.format(curTime);
    }
}
