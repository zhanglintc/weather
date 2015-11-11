package co.zhanglintc.weather;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import co.zhanglintc.weather.common.WeatherUtils;

/**
 * Created by zhanglin on 2015/11/06.
 */
public class BGupdater extends Thread {
    Activity activity;

    TextView city;
    TextView todayTemp;
    TextView todayStatus;
    TextView systemTime;

    ImageView imView;

    Bitmap bmImg;

    String rawJsonData;
    String APIurl;

    BGupdater(Activity activity) {
        this.activity = activity;
        this.APIurl = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=chongqing&nu%20m_of_days=3&format=json&lang=" + WeatherUtils.getLanguge();
        rawJsonData = activity.getResources().getString(R.string.rawJsonData);
    }

    public void run() {
        Log.i("http", "Getting weather data...");
        httpHandler http = new httpHandler();
        String s = http.get(APIurl);

        final String temp_C;
        final String weatherDesc;
        final String iconURL;
        final String systemLang;
        final String cityName;

        try {
            systemLang = WeatherUtils.getLanguge();
            WeatherParser we = new WeatherParser(s);
            temp_C = we.getCurTemp_C();
            cityName = we.getRequestCity();
            if ("en".equals(systemLang)) {
                weatherDesc = we.getCurWeatherDesc();
            }
            else {
                weatherDesc = we.getCurWeatherDescTranslation(systemLang);
            }
            iconURL = we.getCurWeatherIconUrl();
            bmImg  = WeatherUtils.returnBitMap(iconURL);
            Log.i("http", "Current temperature: " + we.getCurTemp_C());
            Log.i("http", "Current condition: " + weatherDesc);
            Log.i("http", "Current weather icon URL: " + we.getCurWeatherIconUrl());
            Log.i("http", "Requested city: " + we.getRequestCity());
            // 登录: http://www.worldweatheronline.com
            // 选择 FULL FORECAST 确认数据正确性
            Log.i("http", "Tomorrow date: " + we.get2ndDayDate());
            Log.i("http", "Tomorrow high: " + we.get2ndDayMaxTempC());
            Log.i("http", "Tomorrow low: "  + we.get2ndDayMinTempC());
            Log.i("http", "Tomorrow condition: "  + we.get2ndDayWeatherDesc());
            Log.i("http", "Day after tomorrow date: " + we.get3rdDayDate());
            Log.i("http", "Day after tomorrow high: " + we.get3rdDayMaxTempC());
            Log.i("http", "Day after tomorrow low: "  + we.get3rdDayMinTempC());
            Log.i("http", "Day after tomorrow condition: " + we.get3rdDayWeatherDesc());

            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            activity.setContentView(R.layout.activity_main);

                            // TextView
                            city = (TextView) activity.findViewById(R.id.city);
                            todayTemp = (TextView) activity.findViewById(R.id.todayTemp);
                            todayStatus = (TextView) activity.findViewById(R.id.todayStatus);
                            systemTime = (TextView) activity.findViewById(R.id.systemTime);

                            // ImageView
                            imView = (ImageView) activity.findViewById(R.id.imview);


                            // Set Text
                            city.setText(cityName);
                            todayTemp.setText(temp_C + "°");
                            todayStatus.setText(weatherDesc);
                            systemTime.setText(WeatherUtils.getSystemTime());

                            // Set Image
                            imView.setImageBitmap(bmImg);
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
