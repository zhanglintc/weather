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

    TextView cityNameView;
    TextView curTempCView;
    TextView curDescView;
    TextView tmrDateView;
    TextView tmrTempCView;
    TextView tmrDescView;
    TextView datDateView;
    TextView datTempCView;
    TextView datDescView;
    TextView sysTimeView;

    Bitmap curDescIcon;
    ImageView curDescIconView;

    String rawJsonData;
    String sysLang;
    String APIurl;

    String curTempC;
    String curDesc;
    String cityName;

    String tmrMaxTemp;
    String tmrMinTemp;
    String datMaxTemp;
    String datMinTemp;

    BGupdater(Activity activity) {
        this.activity = activity;
        this.APIurl = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=55f1fdd05fba23be0a18043d0a017&q=chongqing&nu%20m_of_days=3&format=json&lang=" + WeatherUtils.getLanguge();
        rawJsonData = activity.getResources().getString(R.string.rawJsonData);
    }

    public void run() {
        Log.i("http", "Getting weather data...");
        httpHandler http = new httpHandler();
        String retVal = http.get(APIurl);

        try {
            WeatherParser wp = new WeatherParser(retVal);

            curTempC = wp.getCurTemp_C();
            cityName = wp.getRequestCity();
            tmrMaxTemp = wp.get2ndDayMaxTempC();
            tmrMinTemp = wp.get2ndDayMinTempC();
            datMaxTemp = wp.get3rdDayMaxTempC();
            datMinTemp = wp.get3rdDayMinTempC();

            curDescIcon  = WeatherUtils.returnBitMap(wp.getCurWeatherIconUrl());

            sysLang = WeatherUtils.getLanguge();
            if ("en".equals(sysLang)) {
                curDesc = wp.getCurWeatherDesc();
            }
            else {
                curDesc = wp.getCurWeatherDescTranslation(sysLang);
            }

            Log.i("http", "Current temperature: " + wp.getCurTemp_C());
            Log.i("http", "Current condition: " + curDesc);
            Log.i("http", "Current weather icon URL: " + wp.getCurWeatherIconUrl());
            Log.i("http", "Requested city: " + wp.getRequestCity());
            // 登录: http://www.worldweatheronline.com
            // 选择 FULL FORECAST 确认数据正确性
            Log.i("http", "Tomorrow date: " + wp.get2ndDayDate());
            Log.i("http", "Tomorrow high: " + wp.get2ndDayMaxTempC());
            Log.i("http", "Tomorrow low: "  + wp.get2ndDayMinTempC());
            Log.i("http", "Tomorrow condition: "  + wp.get2ndDayWeatherDesc());
            Log.i("http", "Day after tomorrow date: " + wp.get3rdDayDate());
            Log.i("http", "Day after tomorrow high: " + wp.get3rdDayMaxTempC());
            Log.i("http", "Day after tomorrow low: "  + wp.get3rdDayMinTempC());
            Log.i("http", "Day after tomorrow condition: " + wp.get3rdDayWeatherDesc());

            activity.runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            activity.setContentView(R.layout.activity_main);

                            // TextView
                            cityNameView = (TextView) activity.findViewById(R.id.cityName);
                            curTempCView = (TextView) activity.findViewById(R.id.curTempC);
                            curDescView = (TextView) activity.findViewById(R.id.curDesc);

                            tmrDateView = (TextView) activity.findViewById(R.id.tmrDate);
                            tmrTempCView = (TextView) activity.findViewById(R.id.tmrTempC);
                            tmrDescView = (TextView) activity.findViewById(R.id.tmrDesc);
                            datDateView = (TextView) activity.findViewById(R.id.datDate);
                            datTempCView = (TextView) activity.findViewById(R.id.datTempC);
                            datDescView = (TextView) activity.findViewById(R.id.datDesc);

                            sysTimeView = (TextView) activity.findViewById(R.id.sysTime);

                            // ImageView
                            curDescIconView = (ImageView) activity.findViewById(R.id.curDescIcon);

                            // ------------------------------------------------------------------

                            // Set Text
                            cityNameView.setText(cityName);
                            curTempCView.setText(curTempC + "°C");
                            curDescView.setText(curDesc);

                            tmrTempCView.setText(tmrMinTemp + "~" + tmrMaxTemp + "°C");
                            datTempCView.setText(datMinTemp + "~" + datMaxTemp + "°C");

                            sysTimeView.setText(WeatherUtils.getSystemTime());

                            // Set Image
                            curDescIconView.setImageBitmap(curDescIcon);
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
