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
public class BackgroundUpdateThread extends Thread {
    Bitmap bmImg;
    String APIurl;
    TextView todayTemp;
    TextView todayStatus;
    ImageView imView;
    Activity activity;
    String rawJsonData;

    TextView city;
    TextView systemTime;

    BackgroundUpdateThread(Activity activity) {
        this.activity = activity;
        this.todayTemp = (TextView) activity.findViewById(R.id.todayTemp);
        this.todayStatus = (TextView) activity.findViewById(R.id.todayStatus);
        this.imView = (ImageView) activity.findViewById(R.id.imview);

        this.city = (TextView) activity.findViewById(R.id.city);
        this.systemTime = (TextView) activity.findViewById(R.id.systemTime);

        this.APIurl = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=chongqing&nu%20m_of_days=3&format=json&lang=" + WeatherUtils.getLanguge();

        rawJsonData = activity.getResources().getString(R.string.rawJsonData);
    }

    public void run() {
        // true process
        httpHandler http = new httpHandler();
        Log.i("http", "Getting weather data...");
        String s = http.get(APIurl);

        // fake process
            /*
            String s = rawJsonData;
            try {
                sleep(1000 * 2);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            */
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
            } else {
                weatherDesc = we.getCurWeatherDescTranslation(systemLang);
            }
            iconURL = we.getCurWeatherIconUrl();
            bmImg  = WeatherUtils.returnBitMap(iconURL);
            Log.i("http", "Current temperature: " + temp_C);
            Log.i("http", "Current condition: " + weatherDesc);
            Log.i("http", "Current weather icon URL: " + iconURL);
            Log.i("http", "Requested city: " + cityName);

            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            todayTemp.setText(temp_C + "°");
                            todayStatus.setText(weatherDesc);
                            imView.setImageBitmap(bmImg);
                            city.setText(cityName);
                            systemTime.setText(WeatherUtils.getSystemTime());
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
